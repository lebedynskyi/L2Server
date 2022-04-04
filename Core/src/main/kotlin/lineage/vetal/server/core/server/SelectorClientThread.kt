package lineage.vetal.server.core.server

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import lineage.vetal.server.core.NetworkConfig
import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.client.ClientFactory
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.core.utils.logs.writeInfo
import java.lang.IllegalArgumentException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import kotlin.Exception

class SelectorClientThread<T : Client>(
    private val networkConfig: NetworkConfig,
    private val clientFactory: ClientFactory<T>
) : Thread() {
    private val READ_BUFFER_SIZE = 64 * 1024
    private val WRITE_BUFFER_SIZE = 64 * 1024
    private val TAG = "SocketClientSelector"

    private lateinit var selector: Selector

    private val _selectionAcceptFlow = MutableSharedFlow<T>(1)
    val connectionAcceptFlow = _selectionAcceptFlow.asSharedFlow()

    private val _selectionCloseFlow = MutableSharedFlow<T>(1)
    val connectionCloseFlow = _selectionCloseFlow.asSharedFlow()

    private val _selectionReadFlow = MutableSharedFlow<Pair<T, ReceivablePacket>>(1)
    val connectionReadFlow = _selectionReadFlow.asSharedFlow()

    private val tempWriteBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)

    private val readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
    private val stringBuffer = StringBuffer()

    @Volatile
    var isRunning = true

    override fun run() {
        // TODO does not work loop ?
        while (isRunning) {
            try {
                connectToServer()
            } catch (e: Exception) {
                writeInfo(TAG, "Disconnected from the server Reconnect in 5 sec")
                sleep(5000)
            }
        }
    }

    fun disconnect() {
        isRunning = false
        selector.close()
        selector.wakeup()
        writeInfo(TAG, "Disconnected from the server")
    }

    private fun connectToServer() {
        val address = if (networkConfig.hostname.isBlank() || networkConfig.hostname == "*") {
            throw IllegalArgumentException("Should be direct IP address")
        } else {
            InetSocketAddress(networkConfig.hostname, networkConfig.port)
        }

        selector = Selector.open()
        SocketChannel.open().apply {
            configureBlocking(false)
            connect(address)
            clientFactory.createClient(selector, this)
        }

        while (isRunning) {
            val readyChannels = selector.select()
            if (readyChannels == 0) continue

            val keyIterator = selector.selectedKeys().iterator()
            while (keyIterator.hasNext()) {
                val key = keyIterator.next()
                when (key.readyOps()) {
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
    }

    private fun finishConnection(key: SelectionKey) {
        try {
            val socket = (key.channel() as SocketChannel)
            socket.finishConnect()

            // key might have been invalidated on finishConnect()
            if (key.isValid) {
                key.interestOps(key.interestOps() and SelectionKey.OP_CONNECT.inv())
                writeDebug(TAG, "Connected to ${networkConfig.hostname}:${networkConfig.port}")
                val client = key.attachment() as T
                _selectionAcceptFlow.tryEmit(client)
            } else {
                writeError(TAG, "Something wrong. key is invalid", UnknownError("Unknown"))
            }
        } catch (e: Exception) {
            writeDebug(TAG, "Unable to connect to server ${networkConfig.hostname}:${networkConfig.port}. Try latter")
            sleep(5000)
            connectToServer()
        }
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
            disconnect()
            _selectionCloseFlow.tryEmit(client)
        }
    }

    private fun writePackets(key: SelectionKey) {
        val client = key.attachment() as T
        val connection = client.connection

        connection.writeData(writeBuffer, tempWriteBuffer)

        if (connection.pendingClose) {
            // TODO should be close.
            disconnect()
        } else {
            key.interestOps(key.interestOps() and SelectionKey.OP_WRITE.inv())
        }
    }
}