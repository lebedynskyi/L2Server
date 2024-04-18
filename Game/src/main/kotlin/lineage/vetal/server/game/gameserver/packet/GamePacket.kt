package lineage.vetal.server.game.gameserver.packet

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import vetalll.server.sock.ReadablePacket

abstract class GamePacket : ReadablePacket() {
    override fun read() {}
    abstract fun execute(client: GameClient, context: GameContext)
}