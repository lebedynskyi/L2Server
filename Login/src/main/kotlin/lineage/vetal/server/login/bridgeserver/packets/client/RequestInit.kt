package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.login.bridgeserver.BridgeClient
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.bridgeserver.packets.BridgePacket
import lineage.vetal.server.login.bridgeserver.packets.server.InitOK

class RequestInit : BridgePacket() {
    private val TAG = "RequestInit"

    var serverId: Int = -1

    override fun execute(client: BridgeClient, context: LoginContext) {
        val server = context.loginLobby.getRegisteredServer(serverId)
        if (server != null) {
            client.connection.crypt.init(server.config.bridgeKey.toByteArray())
            client.serverInfo = server
            client.sendPacket(InitOK())
        } else {
            writeInfo(TAG, "Unknown server. Close connection")
            client.saveAndClose()
        }
    }

    override fun read() {
        serverId = readD()
    }
}