package lineage.vetal.server.login.bridgeclient.packets.server

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.game.GameContext

class AuthOk : BridgeGamePacket() {
    override fun execute(client: BridgeClient, context: GameContext) {
    }

    override fun read() {

    }
}