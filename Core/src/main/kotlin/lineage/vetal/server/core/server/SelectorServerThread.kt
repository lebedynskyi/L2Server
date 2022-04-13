package lineage.vetal.server.core.server

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import lineage.vetal.server.core.NetworkConfig
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.core.utils.logs.writeInfo
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel

//TODO implement packet counter and limit per selection.
class SelectorServerThread<T : Client>(
    private val networkConfig: NetworkConfig,
    private val clientFactory: ClientFactory<T>
) : Thread() {
    private val READ_BUFFER_SIZE = 64 * 1024
    private val WRITE_BUFFER_SIZE = 64 * 1024
    private val TAG = "SelectorServerThread"

    private lateinit var selector: Selector
    private lateinit var serverSocket: ServerSocketChannel

    private val _selectionAcceptFlow = MutableSharedFlow<T>(1)
    val connectionAcceptFlow = _selectionAcceptFlow.asSharedFlow()

    private val _selectionCloseFlow = MutableSharedFlow<T>(1)
    val connectionCloseFlow = _selectionCloseFlow.asSharedFlow()

    private val _selectionReadFlow = MutableSharedFlow<Pair<T, ReceivablePacket>>(1)
    val connectionReadFlow = _selectionReadFlow.asSharedFlow()

    private val tempWriteBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)

    private val readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val stringBuffer = StringBuffer(READ_BUFFER_SIZE)

    @Volatile
    var isRunning = true

    override fun run() = openServer()

    private fun openServer() {
        val address = if (networkConfig.hostname.isBlank() || networkConfig.hostname == "*") {
            InetSocketAddress(networkConfig.port)
        } else {
            InetSocketAddress(networkConfig.hostname, networkConfig.port)
        }
        selector = Selector.open()
        serverSocket = ServerSocketChannel.open().apply {
            socket().bind(address)
            configureBlocking(false)
            register(selector, SelectionKey.OP_ACCEPT)
        }
        writeInfo(TAG, "Listening clients on ${networkConfig.hostname}:${networkConfig.port}")

        while (isRunning) {
            val readyChannels = selector.select()
            if (readyChannels == 0) continue

            val keyIterator = selector.selectedKeys().iterator()
            while (keyIterator.hasNext()) {
                val key = keyIterator.next()
                when (key.readyOps()) {
                    SelectionKey.OP_ACCEPT -> acceptConnection()
                    SelectionKey.OP_READ -> readPackets(key)
                    SelectionKey.OP_WRITE -> writePackets(key)
                    SelectionKey.OP_READ or SelectionKey.OP_WRITE -> {
                        writePackets(key)
                        readPackets(key)
                    }
                }

                keyIterator.remove()
            }
        }

        closeServer()
    }


    private fun acceptConnection() {
        val socket = serverSocket.accept().apply { configureBlocking(false) }
        val client = clientFactory.createClient(selector, socket)
        writeInfo(TAG, "New connection $client")
        _selectionAcceptFlow.tryEmit(client)
    }

    private fun readPackets(key: SelectionKey) {
        val client = key.attachment() as T
        val connection = client.connection

        try {
            val packet = connection.readPackets(readBuffer, stringBuffer)

            if (packet != null) {
                _selectionReadFlow.tryEmit(client to packet)
            } else if (connection.pendingClose) {
                closeClient(client, connection)
            }
        } catch (e: Exception) {
            writeError(TAG, "Cannot read packets", e)
            closeClient(client, connection)
        }
    }

    private fun writePackets(key: SelectionKey) {
        val client = key.attachment() as T
        val connection = client.connection
        try {
            connection.writePackets(writeBuffer, tempWriteBuffer)
            key.interestOps(key.interestOps() and SelectionKey.OP_WRITE.inv())

            if (connection.pendingClose) {
                closeClient(client, connection)
            }
        } catch (e: Exception) {
            writeError(TAG, "Cannot write packets", e)
            closeClient(client, connection)
        }
    }

    private fun closeServer() {
        selector.close()
        writeInfo(TAG, "Server closed")
    }

    private fun closeClient(client: T, connection: ClientConnection) {
        writeInfo(TAG, " Closed connection with client $client")
        connection.closeSocket()
        _selectionCloseFlow.tryEmit(client)
    }
}