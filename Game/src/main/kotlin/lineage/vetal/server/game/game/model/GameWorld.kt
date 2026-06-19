package lineage.vetal.server.game.game.model

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.server.*

// Geodata min/max tiles
private const val TILE_X_MIN = 16
private const val TILE_X_MAX = 26
private const val TILE_Y_MIN = 10
private const val TILE_Y_MAX = 25

// Map dimensions
private const val TILE_SIZE = 32768
private const val WORLD_X_MIN = (TILE_X_MIN - 20) * TILE_SIZE
private const val WORLD_X_MAX = (TILE_X_MAX - 19) * TILE_SIZE - 1
private const val WORLD_Y_MIN = (TILE_Y_MIN - 18) * TILE_SIZE
private const val WORLD_Y_MAX = (TILE_Y_MAX - 17) * TILE_SIZE - 1
private const val WORLD_Z_MAX = 16410

// Regions
private const val REGION_SIZE = 2048
private const val REGIONS_X = (WORLD_X_MAX - WORLD_X_MIN + 1) / REGION_SIZE
private const val REGIONS_Y = (WORLD_Y_MAX - WORLD_Y_MIN + 1) / REGION_SIZE

private const val TAG = "GameWorld"

class GameWorld(
    private val context: GameContext
) {
    val players: List<PlayerObject> get() = regions.flatten().flatMap { it.players.values }

    private val regions: Array<Array<WorldRegion>> = Array(REGIONS_X) { x ->
        Array(REGIONS_Y) { y ->
            WorldRegion(x, y)
        }
    }

    init {
        initializeSurroundingRegions()
    }

    fun onPlayerPositionChanged(player: PlayerObject, loc: Position) {
        val oldRegion = player.region
        val newRegion = getRegion(loc)
        if (oldRegion != newRegion && newRegion != null) {
            writeDebug(
                TAG,
                "'${player.name}' changed region from [${oldRegion.tileX},${oldRegion.tileY}] to [${newRegion.tileX},${newRegion.tileY}]"
            )

            val oldSlice = oldRegion.slice(newRegion)
            oldSlice.flatMap { it.players.values }.forEach {
                it.sendPacket(DeleteObject(player))
            }

            oldSlice.flatMap { it.items.values }.forEach {
                player.sendPacket(DeleteObject(it))
            }

            oldSlice.flatMap { it.npc.values }.forEach {
                player.sendPacket(DeleteObject(it))
            }

            val newSlice = newRegion.slice(oldRegion)
            newSlice.flatMap { it.players.values }.forEach {
                it.sendPacket(CharInfo(player))
            }

            newSlice.flatMap { it.players.values }.forEach {
                player.sendPacket(CharInfo(it))
            }

            newSlice.flatMap { it.items.values }.forEach {
                player.sendPacket(SpawnItem(it))
            }

            newSlice.flatMap { it.npc.values }.forEach {
                player.sendPacket(NpcInfo(it))
            }

            oldRegion.removePlayer(player)
            player.region = newRegion
            newRegion.addPlayer(player)
        }
    }

    fun onNpcAdded(npc: NpcObject) {
        val region = getRegion(npc.position)
        if (region != null) {
            region.addNpc(npc)
        }else {
            writeError(TAG, "Npc ${npc.name} is unknown region for position ${npc.position}")
        }
    }

    fun removePlayerFromWorld(client: GameClient, player: PlayerObject) {
        player.region.removePlayer(player)
        context.broadcaster.broadCast(player.region, DeleteObject(player))
        player.isInWorld = false
        client.player = null

        context.gameDatabase.itemsDao.saveItems(player.inventory.items)
        context.gameDatabase.charactersDao.updateCoordinates(player.objectId, player.position)
    }

    fun isRegionExist(x: Int, y: Int): Boolean {
        return x in 0..<REGIONS_X && y in 0..<REGIONS_Y
    }

    fun getRegion(loc: Position): WorldRegion? = getRegion(loc.x, loc.y)

    fun getRegion(x: Int, y: Int): WorldRegion? {
        val regX = (x - WORLD_X_MIN) / REGION_SIZE
        val regY = (y - WORLD_Y_MIN) / REGION_SIZE
        return if (isRegionExist(regX, regY)) regions[regX][regY] else null
    }

    private fun initializeSurroundingRegions() {
        writeInfo(TAG, "Regions size=${regions.flatten().size}")
        writeInfo(TAG, "Init surrounded regions")
        for (regX in 0 until REGIONS_X) {
            for (regY in 0 until REGIONS_Y) {
                val region = regions[regX][regY]
                val surrounding = mutableListOf<WorldRegion>()
                for (srX in regX - 1..regX + 1) {
                    for (srY in regY - 1..regY + 1) {
                        if (isRegionExist(srX, srY) && (srX != regX || srY != regY)) {
                            val surroundRegion = regions[srX][srY]
                            surrounding.add(surroundRegion)
                        }
                    }
                }

                region.surround = surrounding
            }
        }
    }
}
