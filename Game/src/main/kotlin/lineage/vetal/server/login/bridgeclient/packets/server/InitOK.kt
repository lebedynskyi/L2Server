package lineage.vetal.server.login.bridgeclient.packets.server

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.bridgeclient.packets.client.RequestAuth
import vetal.server.writeError

class InitOK : BridgeGamePacket() {
    override fun execute(client: BridgeClient, context: GameContext) {
        val status = client.serverStatus

        if (status == null) {
            client.saveAndClose()
            writeError("InitOK", "Status is null. Close connection")
            return
        }

        client.sendPacket(RequestAuth(status))
    }

    override fun read() {

    }
}