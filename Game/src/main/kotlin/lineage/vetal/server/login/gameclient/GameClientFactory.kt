package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.client.ClientFactory
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class GameClientFactory : ClientFactory<GameClient> {
    override fun createClient(selector: Selector, socket: SocketChannel): GameClient {
        val address = socket.remoteAddress as InetSocketAddress
        val selectionKey = socket.register(selector, SelectionKey.OP_READ)
        val crypt = GameClientCrypt.newInstance()
        val parser = GamePacketParser()
        val connection = GameClientConnection(socket, selector, selectionKey, address, crypt, parser)
        return GameClient(connection,).apply {
            selectionKey.attach(this)
        }
    }
}