package lineage.vetal.server.game.bridgeclient.packets

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.game.game.GameContext
import vetalll.server.sock.ReadablePacket

abstract class BridgeGamePacket : ReadablePacket() {
    abstract fun execute(client: BridgeClient, context: GameContext)
}