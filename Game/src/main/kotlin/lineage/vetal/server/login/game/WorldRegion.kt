package lineage.vetal.server.login.game

import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.gameclient.packet.server.CharInfo
import lineage.vetal.server.login.gameclient.packet.server.DeleteObject
import java.util.concurrent.ConcurrentHashMap

// TODO broadcast issue. Player should receive packet that is sent by this player
class WorldRegion(
    val tileX: Int,
    val tileY: Int
) {
    private val surroundRegions: MutableList<WorldRegion> = mutableListOf()
    private val _players = ConcurrentHashMap<Int, Player>()

    fun addSurroundingRegion(worldRegion: WorldRegion) {
        surroundRegions.add(worldRegion)
    }

    fun addPlayer(player: Player) {
        flattenPlayers().forEach {
            player.sendPacket(CharInfo(it))
        }

        _players[player.objectId] = player
        broadCast(CharInfo(player))
    }

    fun removePlayer(player: Player) {
        _players.remove(player.objectId)
        broadCast(DeleteObject(player))
    }

    fun addNpc(npc: Npc) {

    }

    fun broadCast(packet: SendablePacket, range: Int = Integer.MAX_VALUE) {
        flattenPlayers().forEach {
            it.sendPacket(packet)
        }
    }

    // just store players? it is could be hard for every packet
    private fun flattenPlayers(): List<Player> {
        return _players.values.plus(surroundRegions.map { it._players.values }.flatten())
    }
}