package lineage.vetal.server.game.bridgeclient

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.bridge.BridgeConnection
import lineage.vetal.server.core.bridge.BridgeConnectionCrypt
import vetalll.server.sock.SockClientFactory

class BridgeGameClientFactory : SockClientFactory<BridgeClient>() {
    override fun createClient(): BridgeClient {
        val crypt = BridgeConnectionCrypt()
        val parser = BridgeGamePacketParser()
        val connection = BridgeConnection(parser, crypt)
        return BridgeClient(connection)
    }
}