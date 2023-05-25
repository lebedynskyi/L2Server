package lineage.vetal.server.game.gameserver

import vetal.server.network.ClientFactory
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class GameClientFactory : ClientFactory<GameClient> {
    override fun createClient(selector: Selector, socket: SocketChannel): GameClient {
        val selectionKey = socket.register(selector, SelectionKey.OP_READ)
        val crypt = GameConnectionCrypt.newInstance()
        val parser = GamePacketParser()
        val connection = GameClientConnection(socket, selector, selectionKey, parser, crypt)
        return GameClient(connection,).also {
            selectionKey.attach(it)
        }
    }
}