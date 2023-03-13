package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.utils.ext.toBoolean
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.bridgeserver.packets.BridgeClientPacket

class RequestUpdate : BridgeClientPacket() {
    lateinit var serverStatus: ServerStatus

    override fun read() {
        serverStatus = ServerStatus(
            readD(),
            readD(),
            readC().toBoolean(),
            readS()
        )
    }

    override fun execute(client: BridgeClient, context: LoginContext) {
        context.bridgeLobby.requestUpdate(client, serverStatus)
    }
}