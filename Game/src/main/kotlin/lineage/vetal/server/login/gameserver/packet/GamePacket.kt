package lineage.vetal.server.login.gameserver.packet

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import vetal.server.network.ReceivablePacket

abstract class GamePacket : ReceivablePacket() {
    override fun read() {}
    abstract fun execute(client: GameClient, context: GameContext)
}