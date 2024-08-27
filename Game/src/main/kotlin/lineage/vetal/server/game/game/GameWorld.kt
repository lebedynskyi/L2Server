package lineage.vetal.server.game.game

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.model.WorldRegion
import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.player.SayType
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.GameClientState
import lineage.vetal.server.game.gameserver.packet.server.*
import vetalll.server.sock.WriteablePacket
import java.util.*

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

private const val TAG = "WorldManager"

class GameWorld(
    private val context: GameContext
) {
    val players: List<PlayerObject> get() = regions.flatten().map { it.players.values }.flatten()

    private val regions: Array<Array<WorldRegion>> = Array(REGIONS_X) { x ->
        Array(REGIONS_Y) { y ->
            WorldRegion(x, y)
        }
    }

    init {
        initializeSurroundingRegions()
    }

    fun onPlayerEnterWorld(client: GameClient, player: PlayerObject) {
        val region = getRegion(player.position.x, player.position.y)
        if (region == null) {
            writeError(TAG, "No region found for player ${player.name} and position ${player.position}")
            player.client?.saveAndClose(LeaveWorld())
            return
        }

        client.clientState = GameClientState.WORLD
        client.player = player

        player.lastAccessTime = Calendar.getInstance().timeInMillis
        player.client = client
        player.isInWorld = true
        player.stats.isRunning = true
        player.region = region

        player.sendPacket(UserInfo(player))
        player.sendPacket(InventoryList(player.inventory.items, true))
        player.sendPacket(CreatureSay(SayType.ANNOUNCEMENT, "Welcome in Vetalll L2 World"))

        player.sendPacket(region.surround.map { it.players.values }.flatten().plus(region.players.values).map { CharInfo(it) })
        player.sendPacket(region.surround.map { it.npc.values }.flatten().plus(region.npc.values).map { NpcInfo(it) })
        player.sendPacket(region.surround.map { it.items.values }.flatten().plus(region.items.values).map { SpawnItem(it) })

        broadCast(region, CharInfo(player))

        region.addPlayer(player)
        context.gameDatabase.charactersDao.updateLastAccess(player.objectId, player.lastAccessTime)
        writeDebug(TAG, "Player enter world. ${player.name} -> ${player.id}")
    }

    fun onPlayerRestart(client: GameClient, player: PlayerObject) {
        if (!player.isInWorld) {
            writeError(TAG, "Not active player asked for restart")
            return
        }

        removePlayerFromWorld(client, player)

        client.clientState = GameClientState.LOBBY
        client.sendPacket(RestartResponse.STATIC_PACKET_OK)
        context.gameLobby.onCharSlotSelection(client)
    }

    fun onPlayerQuit(client: GameClient, player: PlayerObject) {
        if (!player.isInWorld) {
            client.saveAndClose()
            writeError(TAG, "Not active player asked for quit. Disconnect after LeaveWorld")
        } else {
            removePlayerFromWorld(client, player)
            client.saveAndClose(LeaveWorld())
        }
    }

    fun onPlayerPositionChanged(player: PlayerObject, loc: Position) {
        val oldRegion = player.region
        val newRegion = getRegion(loc)
        if (oldRegion != newRegion && newRegion != null) {
            writeDebug(TAG, "'${player.name}' changed region from [${oldRegion.tileX},${oldRegion.tileY}] to [${newRegion.tileX},${newRegion.tileY}]")

            val oldSlice = oldRegion.slice(newRegion)
            oldSlice.map { it.players.values}.flatten().forEach {
                it.sendPacket(DeleteObject(player))
            }

            oldSlice.map { it.items.values }.flatten().forEach {
                player.sendPacket(DeleteObject(it))
            }

            oldSlice.map { it.npc.values }.flatten().forEach {
                player.sendPacket(DeleteObject(it))
            }

            val newSlice = newRegion.slice(oldRegion)
            newSlice.map { it.players.values}.flatten().forEach {
                it.sendPacket(CharInfo(player))
            }

            newSlice.map { it.players.values }.flatten().forEach {
                player.sendPacket(CharInfo(it))
            }

            newSlice.map { it.items.values }.flatten().forEach {
                player.sendPacket(SpawnItem(it))
            }

            newSlice.map { it.npc.values }.flatten().forEach {
                player.sendPacket(NpcInfo(it))
            }

            oldRegion.removePlayer(player)
            player.region = newRegion
            newRegion.addPlayer(player)
        }
    }

    fun broadCast(packet: WriteablePacket) {
        players.forEach { it.sendPacket(packet) }
    }

    fun broadCast(region: WorldRegion, packet: WriteablePacket) {
        region.surround.map { it.players.values }.flatten().plus(region.players.values).forEach {
            it.sendPacket(packet)
        }
    }

    fun onNpcAdded(npc: NpcObject) {
        getRegion(npc.position)?.addNpc(npc)
    }

    private fun removePlayerFromWorld(client: GameClient, player: PlayerObject) {
        player.region.removePlayer(player)
        broadCast(player.region, DeleteObject(player))
        player.isInWorld = false
        client.player = null

        context.gameDatabase.itemsDao.saveItems(player.inventory.items)
        context.gameDatabase.charactersDao.updateCoordinates(player.objectId, player.position)
    }

    private fun isRegionExist(x: Int, y: Int): Boolean {
        return x in 0..<REGIONS_X && y in 0..<REGIONS_Y
    }

    private fun getRegion(loc: Position): WorldRegion? = getRegion(loc.x, loc.y)
    private fun getRegion(x: Int, y: Int): WorldRegion? {
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