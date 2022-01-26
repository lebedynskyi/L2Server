package lineage.vetal.server.login.server

import lineage.vetal.server.core.server.ClientConnection
import lineage.vetal.server.core.server.ClientFactory
import lineage.vetal.server.core.server.SocketConnectionFilter
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel

class LoginClientFactory(filter: SocketConnectionFilter) : ClientFactory<LoginClient> {
    override fun createClient(selector: Selector, serverSocket: ServerSocketChannel): LoginClient {
        val socket = serverSocket.accept().apply {
            configureBlocking(false)
        }

        val key = socket.register(selector, SelectionKey.OP_READ).apply {
            attach(socket)
        }

        val clientConnection = ClientConnection(socket, key)
        return LoginClient(clientConnection)
    }
}