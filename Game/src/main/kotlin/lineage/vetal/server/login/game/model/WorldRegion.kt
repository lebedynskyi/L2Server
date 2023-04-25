package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.npc.NpcObject
import lineage.vetal.server.login.game.model.player.PlayerObject
import lineage.vetal.server.login.gameserver.packet.server.CharInfo
import lineage.vetal.server.login.gameserver.packet.server.DeleteObject
import lineage.vetal.server.login.gameserver.packet.server.NpcInfo
import vetal.server.network.SendablePacket
import java.util.concurrent.ConcurrentHashMap

data class WorldRegion(
    val tileX: Int,
    val tileY: Int
) {
    lateinit var surroundingRegions: Array<WorldRegion>
    val players: Map<Int, PlayerObject> get() = _players
    val npc: Map<Int, NpcObject> get() = _npc

    private val _players = ConcurrentHashMap<Int, PlayerObject>()
    private val _npc = ConcurrentHashMap<Int, NpcObject>()

    fun addPlayer(player: PlayerObject) {
        _players[player.objectId] = player

        player.sendPacket(players.values.map { CharInfo(it) })
        player.sendPacket(npc.values.map { NpcInfo(it) })

        broadCast(CharInfo(player))
    }

    fun removePlayer(player: PlayerObject) {
        _players.remove(player.objectId)
        broadCast(DeleteObject(player))
    }

    fun addNpc(npc: NpcObject) {
        _npc[npc.objectId] = npc

        broadCast(NpcInfo(npc))
    }

    fun removeNpc(npc: NpcObject) {
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