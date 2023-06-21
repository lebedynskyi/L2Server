package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.bridge.*
import vetal.server.sock.SockClientFactory

class BridgeFactory : SockClientFactory<BridgeClient>() {
    override fun createClient(): BridgeClient {
        val crypt = BridgeConnectionCrypt()
        val parser = BridgePacketParser()
        val connection = BridgeConnection(parser, crypt)
        return BridgeClient(connection)
    }
}