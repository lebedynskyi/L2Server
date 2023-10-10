package vetal.server.sock

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import vetal.server.writeDebug
import vetal.server.writeError
import vetal.server.writeInfo
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.concurrent.TimeUnit

class SockSelector<T : SockClient>(
    private val hostName: String,
    private val port: Int,
    private val clientFactory: SockClientFactory<T>,
    private val isServer: Boolean,
    private val clientReconnectDelay: Long = 10000,
    private val TAG: String = "SockSelector"
) : Thread() {
    val connectionAcceptFlow get() = _selectionAcceptFlow.asSharedFlow()
    val connectionReadFlow get() = _selectionReadFlow.asSharedFlow()
    val connectionCloseFlow get() = _selectionCloseFlow.asSharedFlow()

    private val _selectionAcceptFlow = MutableSharedFlow<T>(1)
    private val _selectionCloseFlow = MutableSharedFlow<T>(1)
    private val _selectionReadFlow = MutableSharedFlow<Pair<T, ReadablePacket>>(1)

    private val READ_BUFFER_SIZE = 64 * 1024
    private val WRITE_BUFFER_SIZE = 64 * 1024
    private val tempWriteBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val stringBuffer = StringBuffer(READ_BUFFER_SIZE)

    @Volatile
    private var isRunning = false
    private lateinit var selector: Selector

    fun stopSelector() {
        isRunning = false
        selector.close()
        writeInfo(TAG, "Selector stopped")
    }

    override fun run() {
        isRunning = true

        if (isServer) {
            startServer()
        } else {
            startClient()
        }
        writeInfo(TAG, "Selector started")
        loopSelector()
        stopSelector()
    }

    private fun startServer() {
        val address = if (hostName.isBlank() || hostName == "*") {
            InetSocketAddress(port)
        } else {
            InetSocketAddress(hostName, port)
        }

        selector = Selector.open()
        ServerSocketChannel.open().apply {
            socket().bind(address)
            configureBlocking(false)
            register(selector, SelectionKey.OP_ACCEPT)
        }
        writeInfo(TAG, "Listening clients on ${hostName}:${port}")
    }

    private fun startClient() {
        if (!isServer && (hostName.isBlank() || hostName == "*")) {
            throw IllegalArgumentException("ip/host should be direct address for clients")
        }
        val address = InetSocketAddress(hostName, port)

        selector = Selector.open()
        SocketChannel.open().apply {
            configureBlocking(false)
            connect(address)
            register(selector, SelectionKey.OP_CONNECT)
        }
        writeInfo(TAG, "Connecting to server ${hostName}:${port}")
    }

    private fun loopSelector() {
        while (isRunning) {
            val readyKeys = selector.select()
            val selectedKeys = selector.selectedKeys()
            if (readyKeys == 0 && selectedKeys.isEmpty()) {
                writeError(TAG, "Something wrong... Selector woke up without ready keys")
                continue
            }

            val keyIterator = selectedKeys.iterator()
            while (keyIterator.hasNext()) {
                val key = keyIterator.next()
                when (key.readyOps()) {
                    SelectionKey.OP_CONNECT -> finishOutcomeConnection(key)
                    SelectionKey.OP_ACCEPT -> acceptIncomeConnection(key)
                    SelectionKey.OP_READ -> readPackets(key)
                    SelectionKey.OP_WRITE -> writePackets(key)
                    SelectionKey.OP_READ or SelectionKey.OP_WRITE -> {
                        writePackets(key)
                        if (key.isValid) {
                            readPackets(key)
                        }
                    }
                }

                keyIterator.remove()
            }
        }
    }

    // Called on the server side to finish established connection
    private fun acceptIncomeConnection(key: SelectionKey) {
        val serverSocket = key.channel() as ServerSocketChannel
        val socket = serverSocket.accept().apply { configureBlocking(false) }
        val client = clientFactory.createClient(selector, socket)
        writeInfo(TAG, "New connection $client")
        _selectionAcceptFlow.tryEmit(client)
    }

    // Called on the client side to finish established connection
    private fun finishOutcomeConnection(key: SelectionKey) {
        try {
            val socket = (key.channel() as SocketChannel)
            socket.finishConnect()

            // key might have been invalidated on finishConnect()
            if (key.isValid) {
                key.interestOps(key.interestOps() and SelectionKey.OP_CONNECT.inv())
                writeDebug(TAG, "Connected to ${hostName}:${port}")
                clientFactory.createClient(selector, socket)
                val client = key.attachment() as T
                _selectionAcceptFlow.tryEmit(client)
            }
        } catch (e: Exception) {
            writeError(
                TAG,
                "Unable to connect to server ${hostName}:${port}. Try again in ${
                    TimeUnit.MILLISECONDS.toSeconds(clientReconnectDelay)
                } second"
            )
            sleep(clientReconnectDelay)
            startClient()
        }
    }

    private fun readPackets(key: SelectionKey) {
        val client = key.attachment() as T
        val connection = client.connection

        try {
            val result = connection.readPackets(readBuffer, stringBuffer)

            if (!result || connection.pendingClose) {
                closeConnection(client, connection)
            } else {
                while (connection.hasNextPacket()) {
                    val nextPacket = connection.nextPacket() ?: break
                    _selectionReadFlow.tryEmit(client to nextPacket)
                }
            }
        } catch (e: Exception) {
            writeError(TAG, "Cannot read packets", e)
            closeConnection(client, connection)
        }
    }

    private fun writePackets(key: SelectionKey) {
        val client = key.attachment() as T
        val connection = client.connection
        try {
            val writeFinished = connection.writePackets(writeBuffer, tempWriteBuffer)
            if (writeFinished) {
                key.interestOps(key.interestOps() and SelectionKey.OP_WRITE.inv())
            }

            if (connection.pendingClose) {
                closeConnection(client, connection)
            }
        } catch (e: Exception) {
            writeError(TAG, "Cannot write packets", e)
            closeConnection(client, connection)
        }
    }

    private fun closeConnection(client: T, connection: SockConnection) {
        writeInfo(TAG, " Close connection with client $client")
        connection.closeSocket()
        _selectionCloseFlow.tryEmit(client)

        if (!isServer) {
            startClient()
        }
    }
}