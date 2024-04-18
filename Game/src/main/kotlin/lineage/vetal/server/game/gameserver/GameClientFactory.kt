package lineage.vetal.server.game.gameserver

import vetalll.server.sock.SockClientFactory

class GameClientFactory : SockClientFactory<GameClient>() {
    override fun createClient(): GameClient {
        val crypt = GameConnectionCrypt.newInstance()
        val parser = GamePacketParser()
        val connection = GameClientConnection(parser, crypt)
        return GameClient(connection,)
    }
}