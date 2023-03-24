package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.gameserver.packet.server.CharInfo
import lineage.vetal.server.login.gameserver.packet.server.NpcInfo
import vetal.server.network.SendablePacket
import java.util.concurrent.ConcurrentHashMap

data class WorldRegion(
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

        player.sendPacket(players.values.map { CharInfo(it) })
        player.sendPacket(npc.values.map { NpcInfo(it) })

        broadCast(CharInfo(player))
    }

    fun removePlayer(player: Player) {
        _players.remove(player.objectId)
    }

    fun addNpc(npc: Npc) {
        _npc[npc.objectId] = npc

        broadCast(NpcInfo(npc))
    }

    fun removeNpc(npc: Npc) {
        _npc.remove(npc.objectId)
    }

    fun broadCast(packet: SendablePacket) {
        players.values.plus(surroundingRegions.map { it.players.values }.flatten()).forEach {
            it.sendPacket(packet)
        }
    }

    override fun toString(): String {
        return "Region ($tileX,$tileY). Players=${players.count()}, NPCs=${npc.count()}"
    }
}