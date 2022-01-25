package lineage.vetal.server.core.server

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import lineage.vetal.server.core.settings.NetworkConfig
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel


private const val TAG = "SelectorServer"

class SocketSelectorThread(
    private val configIpAddress: NetworkConfig,
    private val connectionFactory: SocketConnectionFactory
) {
    private lateinit var selector: Selector
    private lateinit var serverSocket: ServerSocketChannel
    private val _selectionFlow = MutableSharedFlow<ClientConnection>(1)

    var isRuning = true

    fun start(): SharedFlow<ClientConnection> {
        // TODO need 2 different flow for connected / readable
        Thread {
            listenForClients()
        }.start()
        return _selectionFlow.asSharedFlow()
    }

    private fun listenForClients() {
        selector = Selector.open()
        serverSocket = ServerSocketChannel.open().apply {
            socket().bind(InetSocketAddress(configIpAddress.hostname, configIpAddress.port))
            configureBlocking(false)
            register(selector, SelectionKey.OP_ACCEPT)
        }
        writeInfo(TAG, "Listening clients on ${configIpAddress.hostname}:${configIpAddress.port}")

        while (isRuning) {
            writeDebug(TAG, "waiting for selected key")
            val readyChannels = selector.select()
            if (readyChannels == 0) continue

            val keyIterator = selector.selectedKeys().iterator()
            while (keyIterator.hasNext()) {
                val key = keyIterator.next()
                when {
                    key.isAcceptable -> {
                        writeDebug(TAG, "New accept request")
                        val socketConnection = connectionFactory.createConnection(selector, serverSocket)
                        _selectionFlow.tryEmit(ClientConnection(socketConnection, selector))
                    }
                    key.isConnectable -> {
                        writeDebug(TAG, "isConnectable")
                    }
                    key.isReadable -> {
                        writeDebug(TAG, "isReadable")
                    }
                    key.isWritable -> {
                        writeDebug(TAG, "isWritable")
                    }
                }

                keyIterator.remove()
            }
        }
    }
}