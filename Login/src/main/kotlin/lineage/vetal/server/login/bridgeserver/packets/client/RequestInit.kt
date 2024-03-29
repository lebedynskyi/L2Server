package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.bridgeserver.packets.BridgeClientPacket

class RequestInit : BridgeClientPacket() {
    var serverId: Int = -1

    override fun read() {
        serverId = readD()
    }

    override fun execute(client: BridgeClient, context: LoginContext) {
        context.bridgeLobby.requestInit(client, serverId)
    }
}