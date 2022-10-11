package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.login.bridgeserver.BridgeClient
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.utils.ext.toBoolean
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.bridgeserver.packets.BridgePacket
import lineage.vetal.server.login.bridgeserver.packets.server.AuthOk

class RequestAuth : BridgePacket() {
    private val TAG = "RequestAuth"

    lateinit var serverStatus: ServerStatus

    override fun execute(client: BridgeClient, context: LoginContext) {
        val result = context.loginLobby.updateServerStatus(serverStatus)
        if (result) {
            writeInfo(TAG, "Server status received $serverStatus")
            client.sendPacket(AuthOk())
        } else {
            client.saveAndClose()
        }
    }

    override fun read() {
        serverStatus = ServerStatus(
            readD(),
            readD(),
            readC().toBoolean(),
            readS()
        )
    }
}