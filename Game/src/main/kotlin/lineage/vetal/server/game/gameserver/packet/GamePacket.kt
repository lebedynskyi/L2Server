package lineage.vetal.server.game.gameserver.packet

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import vetal.server.network.ReceivablePacket

abstract class GamePacket : ReceivablePacket() {
    override fun read() {}
    abstract fun execute(client: GameClient, context: GameContext)
}