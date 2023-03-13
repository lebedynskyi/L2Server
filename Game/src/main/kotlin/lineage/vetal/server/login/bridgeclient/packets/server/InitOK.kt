package lineage.vetal.server.login.bridgeclient.packets.server

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.bridgeclient.packets.client.RequestAuth
import lineage.vetal.server.login.game.GameContext
import vetal.server.writeError

class InitOK : BridgeGamePacket() {
    override fun execute(client: BridgeClient, context: GameContext) {
        val serverStatus = client.serverInfo?.status
        if (serverStatus == null) {
            client.saveAndClose()
            writeError("InitOK", "Status is null. Close connection")
            return
        }

        client.sendPacket(RequestAuth(serverStatus))
    }

    override fun read() {

    }
}