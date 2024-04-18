package lineage.vetal.server.game.game.manager

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.WorldRegion
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.player.SayType
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.GameClientState
import lineage.vetal.server.game.gameserver.packet.server.*
import vetalll.server.sock.WriteablePacket
import vetalll.server.sock.writeInfo
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

class WorldManager(
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

    fun broadCast(packet: WriteablePacket) {
        players.forEach { it.sendPacket(packet) }
    }

    fun onNpcAdded(npc: NpcObject) {
        getRegion(npc.position)?.addNpc(npc)
    }

    fun onPlayerEnterWorld(client: GameClient, player: PlayerObject) {
        player.lastAccessTime = Calendar.getInstance().timeInMillis
        player.client = client
        client.clientState = GameClientState.WORLD
        client.player = player
        player.isActive = true
        player.isRunning = true
        player.sendPacket(UserInfo(player))
        player.sendPacket(InventoryList(player.inventory.items, true))
        player.sendPacket(CreatureSay(SayType.ANNOUNCEMENT, "This is startup message from  Server!"))
        context.gameDatabase.charactersDao.updateLastAccess(player.objectId, (Calendar.getInstance().time.time / 1000).toInt())
        addPlayerToWorld(player)
    }

    fun onPlayerRestart(client: GameClient, player: PlayerObject) {
        client.player = null
        client.clientState = GameClientState.LOBBY
        onPlayerQuit(client, player)
        client.sendPacket(RestartResponse.STATIC_PACKET_OK)
        context.gameLobby.onCharSlotSelection(client)
    }

    fun onPlayerQuit(client: GameClient, player: PlayerObject) {
        player.region.removePlayer(player)
        if (player.isActive) {
            client.saveAndClose(LeaveWorld())
            context.gameDatabase.itemsDao.saveInventory(player.inventory.items)
            context.gameDatabase.charactersDao.updateCoordinates(player.objectId, player.position)
        }
        player.isActive = false
    }

    fun onPlayerPositionChanged(player: PlayerObject, loc: Position) {
        val currentRegion = player.region
        val newRegion = getRegion(loc)
        if (currentRegion != newRegion && newRegion != null) {
            player.region = newRegion
            newRegion.addPlayer(player)
            currentRegion.removePlayer(player)
        }
    }

    private fun addPlayerToWorld(player: PlayerObject) {
        val region = getRegion(player.position.x, player.position.y)
        if (region == null) {
            writeInfo(TAG, " No region found for player ${player.name} and position ${player.position}")
            player.client?.saveAndClose(LeaveWorld())
            return
        }
        player.region = region
        region.addPlayer(player)
    }

    private fun isRegionExist(x: Int, y: Int): Boolean {
        return x in 0 until REGIONS_X && y in 0 until REGIONS_Y
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
                for (srX in -1..1) {
                    for (srY in -1..1) {
                        val sorX = regX + srX
                        val sorY = regY + srY
                        if (isRegionExist(sorX, sorY) && sorX != regX && sorY != regY) {
                            val surroundRegion = regions[sorX][sorY]
                            surrounding.add(surroundRegion)
                        }
                    }
                }

                region.surroundingRegions = surrounding.toTypedArray()
            }
        }
        writeInfo(TAG, "Init surrounded regions complete")
    }
}