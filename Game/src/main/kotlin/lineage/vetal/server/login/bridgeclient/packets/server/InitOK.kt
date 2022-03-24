package lineage.vetal.server.login.bridgeclient.packets.server

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.bridgeclient.BridgeGameClient
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.bridgeclient.packets.client.RequestAuth

class InitOK : BridgeGamePacket() {
    override fun execute(client: BridgeGameClient, context: GameContext) {
        client.sendPacket(RequestAuth(client.status))
    }

    override fun read() {

    }
}