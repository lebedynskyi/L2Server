package lineage.vetal.server.core.server

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.core.client.ClientFactory
import lineage.vetal.server.core.config.NetworkConfig
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel


class SocketSelectorThread<T : Client>(
    private val networkConfig: NetworkConfig,
    private val connectionFactory: ClientFactory<T>
) : Thread() {
    private val READ_BUFFER_SIZE = 64 * 1024
    private val WRITE_BUFFER_SIZE = 64 * 1024
    private val TAG = "SocketSelectorThread"

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

    private val tempReadBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val stringBuffer = StringBuffer()

    @Volatile
    var isRunning = true

    override fun run() {
        openServer()
    }

    fun stopServer() {
        selector.close()
    }

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
                    SelectionKey.OP_ACCEPT -> createConnection()
                    SelectionKey.OP_CONNECT -> finishConnection(key)
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

    private fun closeServer() {
        writeInfo(TAG, "Server closed")
    }

    private fun finishConnection(key: SelectionKey) {
        key.channel().close()
    }

    private fun createConnection() {
        writeDebug(TAG, "Create connection")
        val client = connectionFactory.createClient(selector, serverSocket)
        _selectionAcceptFlow.tryEmit(client)
    }

    private fun readPackets(key: SelectionKey) {
        val client = key.attachment() as T
        val connection = client.connection

        writeDebug(TAG, "Read packets from $client")
        val packet = connection.readData(readBuffer, stringBuffer)

        if (packet != null) {
            _selectionReadFlow.tryEmit(client to packet)
        } else {
            writeDebug(TAG, "0 packets read. Close connection")
            finishConnection(key)
            _selectionCloseFlow.tryEmit(client)
        }
    }

    private fun writePackets(key: SelectionKey) {
        val client = key.attachment() as T
        val connection = client.connection

        writeDebug(TAG, "Write packets to $client")
        connection.writeData(writeBuffer, tempWriteBuffer)

        if (connection.pendingClose) {
            finishConnection(key)
        } else {
            key.interestOps(key.interestOps() and SelectionKey.OP_WRITE.inv())
        }
    }
}