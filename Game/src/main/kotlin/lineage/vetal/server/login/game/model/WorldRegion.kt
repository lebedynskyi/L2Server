package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.item.ItemObject
import lineage.vetal.server.login.game.model.npc.NpcObject
import lineage.vetal.server.login.game.model.player.PlayerObject
import lineage.vetal.server.login.gameserver.packet.server.CharInfo
import lineage.vetal.server.login.gameserver.packet.server.DeleteObject
import lineage.vetal.server.login.gameserver.packet.server.NpcInfo
import lineage.vetal.server.login.gameserver.packet.server.SpawnItem
import vetal.server.network.SendablePacket
import java.util.concurrent.ConcurrentHashMap

// TODO need to understand design of this class. Is it just holder for items ? DO I need Broadcast Manager ?
data class WorldRegion(
    val tileX: Int,
    val tileY: Int
) {
    lateinit var surroundingRegions: Array<WorldRegion>
    val players: Map<Int, PlayerObject> get() = _players
    val npc: Map<Int, NpcObject> get() = _npc
    val items: Map<Int, ItemObject> get() = _items

    private val _players = ConcurrentHashMap<Int, PlayerObject>()
    private val _npc = ConcurrentHashMap<Int, NpcObject>()
    private val _items = ConcurrentHashMap<Int, ItemObject>()

    fun addItem(itemObject: ItemObject) {
        _items[itemObject.objectId] = itemObject
    }

    fun removeItem(itemObject: ItemObject) {
        _items.remove(itemObject.objectId)
    }

    fun addPlayer(player: PlayerObject) {
        _players[player.objectId] = player

        player.sendPacket(players.values.map { CharInfo(it) })
        player.sendPacket(npc.values.map { NpcInfo(it) })
        player.sendPacket(items.values.map { SpawnItem(it) })

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

    fun broadCastRegion(packet: SendablePacket) {
        players.values.forEach {
            it.sendPacket(packet)
        }
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