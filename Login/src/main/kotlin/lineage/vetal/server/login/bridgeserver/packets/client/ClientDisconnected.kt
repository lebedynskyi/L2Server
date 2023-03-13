package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.bridgeserver.packets.BridgeClientPacket

class ClientDisconnected : BridgeClientPacket() {
    override fun read() {

    }

    override fun execute(client: BridgeClient, context: LoginContext) {
        context.bridgeLobby.onClientDisconnected(client)
    }
}