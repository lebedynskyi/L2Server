package lineage.vetal.server.core.server

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.core.client.ClientFactory
import lineage.vetal.server.core.settings.NetworkConfig
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel


private const val TAG = "SelectorServer"

class SocketSelectorThread<T: Client>(
    private val networkConfig: NetworkConfig,
    private val connectionFactory: ClientFactory<T>
) {
    private lateinit var selector: Selector
    private lateinit var serverSocket: ServerSocketChannel

    private val _selectionAcceptFlow = MutableSharedFlow<Client>(1)
    val connectionAcceptFlow = _selectionAcceptFlow.asSharedFlow()

    private val _selectionCloseFlow = MutableSharedFlow<ClientConnection>(1)
    val connectionCloseFlow = _selectionCloseFlow.asSharedFlow()

    private val _selectionReadFlow = MutableSharedFlow<ClientConnection>(1)
    val connectionReadFlow = _selectionReadFlow.asSharedFlow()

    private val socketReader = SocketReader()
    private val socketWriter = SocketWriter()

    @Volatile
    var isRunning = true

    fun start() = Thread { openServer() }.start()

    fun stop() {
        isRunning = false
        selector.wakeup()
    }

    private fun openServer() {
        val address =  if (networkConfig.hostname.isBlank() || networkConfig.hostname == "*") {
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
            writeDebug(TAG, "waiting for selected key")
            val readyChannels = selector.select()
            if (readyChannels == 0) continue

            val keyIterator = selector.selectedKeys().iterator()
            while (keyIterator.hasNext()) {
                val key = keyIterator.next()
                when {
                    key.isAcceptable -> {
                        val client = connectionFactory.createClient(selector, serverSocket)
                        _selectionAcceptFlow.tryEmit(client)
                    }
                    key.isReadable -> {
                        val client = key.attachment() as T
                    }
                    key.isWritable -> {
                        val client = key.attachment() as T
                    }
                }

                keyIterator.remove()
            }
        }

        closeServer()
    }

    private fun closeServer() {

    }
}