package lineage.vetal.server.game.bridgeclient.packets

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.game.game.GameContext
import vetal.server.network.ReceivablePacket

abstract class BridgeGamePacket : ReceivablePacket() {
    abstract fun execute(client: BridgeClient, context: GameContext)
}