package lineage.vetal.server.game.bridgeclient.packets.server

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.game.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.game.game.GameContext

class BridgeConnected : BridgeGamePacket() {
    override fun read() {

    }

    override fun execute(client: BridgeClient, context: GameContext) {
        context.gameLobby.onConnectedToBridge(client)
    }
}