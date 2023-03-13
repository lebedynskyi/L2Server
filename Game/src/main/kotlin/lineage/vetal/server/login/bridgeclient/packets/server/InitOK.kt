package lineage.vetal.server.login.bridgeclient.packets.server

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.game.GameContext

class InitOK : BridgeGamePacket() {
    override fun read() {

    }

    override fun execute(client: BridgeClient, context: GameContext) {
        context.gameLobby.onInitOk(client)
    }
}