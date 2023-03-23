package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.player.Player
import vetal.server.network.SendablePacket
import java.util.concurrent.ConcurrentHashMap

class WorldRegion(
    val tileX: Int,
    val tileY: Int
) {
    lateinit var surroundingRegions: Array<WorldRegion>
    val players: Map<Int, Player> get() = _players
    val npc: Map<Int, Npc> get() = _npc

    private val _players = ConcurrentHashMap<Int, Player>()
    private val _npc = ConcurrentHashMap<Int, Npc>()

    fun addPlayer(player: Player) {
        _players[player.objectId] = player
    }

    fun removePlayer(player: Player) {
        _players.remove(player.objectId)
    }

    fun addNpc(npc: Npc) {
        _npc[npc.objectId] = npc
    }

    fun removeNpc(npc: Npc) {
        _npc.remove(npc.objectId)
    }

    fun broadCast(packet: SendablePacket) {
        surroundingRegions.map { it.players.values }.flatten().plus(players.values).forEach {
            it.sendPacket(packet)
        }
    }
}