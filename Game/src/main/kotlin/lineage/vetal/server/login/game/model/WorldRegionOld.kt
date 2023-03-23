package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.gameserver.packet.server.CharInfo
import lineage.vetal.server.login.gameserver.packet.server.DeleteObject
import lineage.vetal.server.login.gameserver.packet.server.NpcInfo
import vetal.server.network.SendablePacket
import java.util.concurrent.ConcurrentHashMap

//// TODO broadcast issue. Player should receive packet that is sent by this player
//class WorldRegionOld(
//    val tileX: Int,
//    val tileY: Int
//) {
//    private val surroundRegions: MutableList<WorldRegionOld> = mutableListOf()
//    private val _players = ConcurrentHashMap<Int, Player>()
//    val _npc = ConcurrentHashMap<Int, Npc>()
//
//    fun addSurroundingRegion(worldRegionOld: WorldRegionOld) {
//        if (worldRegionOld.tileX == tileX && worldRegionOld.tileY == tileY) {
//            return
//        }
//
//        surroundRegions.add(worldRegionOld)
//    }
//
//    fun addNpc(npc: Npc) {
//        _npc[npc.objectId] = npc
//    }
//
//    fun addPlayer(player: Player) {
//        flattenPlayers().forEach {
//            player.sendPacket(CharInfo(it))
//        }
//
//        broadCast(CharInfo(player))
//        _players[player.objectId] = player
//
//        flattenNpc().forEach {
//            player.sendPacket(NpcInfo(it))
//        }
//    }
//
//    fun removePlayer(player: Player) {
//        _players.remove(player.objectId)
//        broadCast(DeleteObject(player))
//    }
//
//    fun broadCast(packet: SendablePacket, owner: Creature? = null, range: Int = Integer.MAX_VALUE) {
//        flattenPlayers().forEach {
//            it.sendPacket(packet)
//        }
//    }
//
//    // just store players? it is could be hard for every packet
//    private fun flattenPlayers(): List<Player> {
//        return _players.values.plus(surroundRegions.map { it._players.values }.flatten())
//    }
//
//    private fun flattenNpc(): List<Npc> {
//        return _npc.values.plus(surroundRegions.map { it._npc.values }.flatten())
//    }
//}