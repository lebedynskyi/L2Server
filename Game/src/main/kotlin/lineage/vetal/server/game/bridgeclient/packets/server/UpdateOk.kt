package lineage.vetal.server.game.bridgeclient.packets.server

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.game.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.game.game.GameContext

class UpdateOk : BridgeGamePacket() {
    override fun execute(client: BridgeClient, context: GameContext) {

    }

    override fun read() {

    }
}