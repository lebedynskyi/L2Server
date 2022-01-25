package lineage.vetal.server.core.server

import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel


class SocketConnectionFactory(
    private val connectionFilter: SocketConnectionFilter
) {
    fun createConnection(selector: Selector, serverSocket: ServerSocketChannel): SocketChannel {
        return serverSocket.accept().apply {
            configureBlocking(false)
            register(selector, SelectionKey.OP_WRITE)
        }
    }
}