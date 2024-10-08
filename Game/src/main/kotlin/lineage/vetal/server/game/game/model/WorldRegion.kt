package lineage.vetal.server.game.game.model

import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import java.util.concurrent.ConcurrentHashMap

data class WorldRegion(
    val tileX: Int,
    val tileY: Int
) {
    lateinit var surround: List<WorldRegion>

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
    }

    fun removePlayer(player: PlayerObject) {
        _players.remove(player.objectId)
    }

    fun addNpc(npc: NpcObject) {
        _npc[npc.objectId] = npc
    }

    override fun toString(): String {
        return "Region ($tileX,$tileY). Players=${players.count()}, NPCs=${npc.count()}, Items=${items.count()}"
    }

    fun getVisibleNpc(objectId: Int): NpcObject? {
        var npc = npc[objectId]
        if (npc != null) {
            return npc
        }

        surround.forEach {
            npc = it.npc[objectId]
            if (npc != null) {
                return npc
            }
        }
        return null
    }

    fun getVisiblePlayer(objectId: Int): PlayerObject? {
        var player = players[objectId]
        if (player != null) {
            return player
        }

        surround.forEach {
            player = it.players[objectId]
            if (player != null) {
                return player
            }
        }
        return null
    }

    fun getVisibleItem(objectId: Int): ItemObject? {
        var item = items[objectId]
        if (item != null) {
            return item
        }

        surround.forEach {
            item = it.items[objectId]
            if (item != null) {
                return item
            }
        }
        return null
    }

    fun slice(another: WorldRegion): List<WorldRegion> {
        return buildList {
            surround.forEach { sr ->
                if (!another.surround.contains(sr) && sr != another) {
                    add(sr)
                }
            }
        }
    }
}