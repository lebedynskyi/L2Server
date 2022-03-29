package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.core.client.BridgeClient
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.bridgeserver.packets.BridgePacket

class BridgeClientDisconnected : BridgePacket() {
    override fun execute(client: BridgeClient, context: LoginContext) {

    }

    override fun read() {

    }
}