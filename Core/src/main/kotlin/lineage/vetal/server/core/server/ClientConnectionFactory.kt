package lineage.vetal.server.core.server

import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel


class ClientConnectionFactory(
    private val connectionFilter: SocketConnectionFilter
) {
    fun createConnection(selector: Selector, serverSocket: ServerSocketChannel): ClientConnection {
        val socket = serverSocket.accept().apply {
            configureBlocking(false)
        }

        val key = socket.register(selector, SelectionKey.OP_WRITE).apply {
            attach(socket)
        }

        return ClientConnection(socket, key)
    }
}