package vetal.server.network

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

//TODO implement packet counter and limit per selection.
class SelectorThread<T : Client>(
    private val hostName: String,
    private val port: Int,
    private val clientFactory: ClientFactory<T>,
    private val isServer: Boolean,
    private val TAG: String = "Selector"
) : Thread() {
    private val READ_BUFFER_SIZE = 64 * 1024
    private val WRITE_BUFFER_SIZE = 64 * 1024

    val connectionAcceptFlow get() = _selectionAcceptFlow.asSharedFlow()
    val connectionReadFlow get() = _selectionReadFlow.asSharedFlow()
    val connectionCloseFlow get() = _selectionCloseFlow.asSharedFlow()

    private val _selectionAcceptFlow = MutableSharedFlow<T>(1)
    private val _selectionCloseFlow = MutableSharedFlow<T>(1)
    private val _selectionReadFlow = MutableSharedFlow<Pair<T, ReceivablePacket>>(1)

    private val tempWriteBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val stringBuffer = StringBuffer(READ_BUFFER_SIZE)

    @Volatile
    private var isRunning = false
    private lateinit var selector: Selector

    override fun run() {
        isRunning = true

        if (isServer) {
            startServer()
        } else {
            startClient()
        }
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
        writeInfo(TAG, "Listening game clients on ${hostName}:${port}")
        loopSelector()
        closeSelector()
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
        writeInfo(TAG, "Connecting to ${hostName}:${port}")
        loopSelector()
        closeSelector()
    }

    private fun loopSelector() {
        while (isRunning) {
            val readyChannels = selector.select()
            if (readyChannels == 0) {
                writeError(TAG, "Something wrong.. Selector woke up without ready keys")
                continue
            }

            val keyIterator = selector.selectedKeys().iterator()
            while (keyIterator.hasNext()) {
                val key = keyIterator.next()
                when (key.readyOps()) {
                    SelectionKey.OP_CONNECT -> finishConnection(key)
                    SelectionKey.OP_ACCEPT -> acceptConnection(key)
                    SelectionKey.OP_READ -> readPackets(key)
                    SelectionKey.OP_WRITE -> writePackets(key)
                    SelectionKey.OP_READ or SelectionKey.OP_WRITE -> {
                        writePackets(key)
                        if (key.isValid){
                            readPackets(key)
                        }
                    }
                }

                keyIterator.remove()
            }
        }
    }

    fun closeSelector() {
        isRunning = false
        selector.close()
        writeInfo(TAG, "Selector closed")
    }

    // Called on the server side to finish established connection
    private fun acceptConnection(key: SelectionKey) {
        val serverSocket = key.channel() as ServerSocketChannel
        val socket = serverSocket.accept().apply { configureBlocking(false) }
        val client = clientFactory.createClient(selector, socket)
        writeInfo(TAG, "New connection $client")
        _selectionAcceptFlow.tryEmit(client)
    }

    // Called on the client side to finish established connection
    private fun finishConnection(key: SelectionKey) {
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
            } else {
                writeError(TAG, "Something wrong. key is invalid", UnknownError("Unknown"))
            }
        } catch (e: Exception) {
            writeError(TAG, "Unable to connect to server ${hostName}:${port}. Try again in 5 second")
            sleep(5000)
            startClient()
        }
    }

    private fun readPackets(key: SelectionKey) {
        val client = key.attachment() as T
        val connection = client.connection

        try {
            // TODO should be list of packets
            val packet = connection.readPackets(readBuffer, stringBuffer)

            if (packet != null) {
                _selectionReadFlow.tryEmit(client to packet)
            } else if (connection.pendingClose) {
                closeConnection(client, connection)
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

    private fun closeConnection(client: T, connection: ClientConnection) {
        writeInfo(TAG, " Closed connection with client $client")
        connection.closeSocket()
        _selectionCloseFlow.tryEmit(client)

        if (!isServer) {
            startClient()
        }
    }
}