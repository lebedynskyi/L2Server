package lineage.vetal.server.game.game.manager

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.WorldRegion
import vetalll.server.sock.WriteablePacket

class BroadcastManager(
    private val gameContext: GameContext
) {
    fun broadCast(packet: WriteablePacket) {
        gameContext.gameWorld.players.forEach { it.sendPacket(packet) }
    }

    fun broadCast(region: WorldRegion, packet: WriteablePacket) {
        region.surround.map { it.players.values }.flatten().plus(region.players.values).forEach {
            it.sendPacket(packet)
        }
    }
}
