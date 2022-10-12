package lineage.vetal.server.login.bridgeclient.packets

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.game.GameContext
import vetal.server.network.ReceivablePacket

abstract class BridgeGamePacket : ReceivablePacket() {
    abstract fun execute(client: BridgeClient, context: GameContext)
}