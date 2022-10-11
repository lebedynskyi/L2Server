package lineage.vetal.server.login.bridgeclient.packets

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.bridgeclient.BridgeGameClient
import vetal.server.network.ReceivablePacket

abstract class BridgeGamePacket : ReceivablePacket() {
    abstract fun execute(client: BridgeGameClient, context: GameContext)
}