package lineage.vetal.server.login.game.manager

import lineage.vetal.server.login.db.GameDatabase
import lineage.vetal.server.login.game.model.WorldRegion
import lineage.vetal.server.login.game.model.location.Location
import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.game.model.player.SayType
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.GameClientState
import lineage.vetal.server.login.gameserver.packet.server.*
import vetal.server.network.SendablePacket
import vetal.server.writeDebug
import vetal.server.writeError
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
    private val npc: List<Npc>,
    private val gameDatabase: GameDatabase
) {
    val players: List<Player> get() = regions.flatten().map { it.players.values }.flatten()
    val regions: Array<Array<WorldRegion>> = Array(REGIONS_X) { x -> Array(REGIONS_Y) { y -> WorldRegion(x, y) } }

    init {
        writeDebug(TAG, "World start Init")

        for (regX in 0 until REGIONS_X) {
            for (regY in 0 until REGIONS_Y) {
                val region = regions[regX][regY]
                val surrounding = mutableListOf<WorldRegion>()
                for (srX in -1..1) {
                    for (srY in -1..1) {
                        val sorX = regX + srX
                        val sorY = regY + srY
                        if (isRegionExist(sorX, sorY) && sorX != regX && sorY != regY) {
                            val surround = regions[sorX][sorY]
                            surrounding.add(surround)
                        }
                    }
                }

                region.surroundingRegions = surrounding.toTypedArray()
            }
        }

        npc.forEach {
            getRegion(it.position)?.addNpc(it)
        }

        writeDebug(TAG, "World finish Init")
    }

    fun isRegionExist(x: Int, y: Int) = x in 0 until REGIONS_X && y in 0 until REGIONS_Y
    fun getRegion(loc: Location): WorldRegion? = getRegion(loc.x, loc.y)

    fun getRegion(x: Int, y: Int): WorldRegion? {
        val regX = (x - WORLD_X_MIN) / REGION_SIZE
        val regY = (y - WORLD_Y_MIN) / REGION_SIZE
        return if (isRegionExist(regX, regY)) regions[regX][regY] else null
    }

    fun broadCast(packet: SendablePacket) {
        players.forEach { it.sendPacket(packet) }
    }

    fun onPlayerEnteredWorld(client: GameClient, player: Player) {
        player.lastAccessTime = Calendar.getInstance().timeInMillis
        player.client = client
        player.isActive = true
        client.clientState = GameClientState.WORLD
        client.player = player
        player.sendPacket(UserInfo(player))
        player.sendPacket(CreatureSay(SayType.ANNOUNCEMENT, "Hello on Mega server"))
        spawn(player)
    }

    fun onPlayerRestart(client: GameClient, player: Player) {
        client.player = null
        client.sendPacket(RestartResponse.STATIC_PACKET_OK)
        client.sendPacket(CharSlotList(client, client.characterSlots))
        client.clientState = GameClientState.LOBBY
        player.region.removePlayer(player)

        if (player.isActive) {
            player.isActive = false
            gameDatabase.charactersDao.updateCoordinates(player.id, player.position)
        }
    }

    fun onPlayerQuitWorld(client: GameClient, player: Player) {
        player.region.removePlayer(player)
        if (player.isActive) {
            // TODO save fully
            client.saveAndClose(LeaveWorld())
            gameDatabase.charactersDao.updateCoordinates(player.id, player.position)
        }
        player.isActive = false
    }

    fun onPlayerMoved(player: Player, loc: Location) {
        val currentRegion = player.region
        val newRegion = getRegion(loc)
        if (currentRegion != newRegion && newRegion != null) {
            player.region = newRegion
            newRegion.addPlayer(player)
            currentRegion.removePlayer(player)
            //TODO Check it with Adrenaline. surrounding regions does not see this player anymore. need to remove it ? Client handle it ?
        }
    }

    fun spawn(player: Player) {
        val region = getRegion(player.position.x, player.position.y)
        if (region == null) {
            writeError(TAG, " No region found for player ${player.name} and position ${player.position}")
            player.client?.saveAndClose(LeaveWorld())
            return
        }
        player.region = region
        region.addPlayer(player)
    }

    fun spawn(npc: Npc) {
        val region = getRegion(npc.position.x, npc.position.y)
        if (region == null) {
            writeError(TAG, " No region found for npc ${npc.name} and position ${npc.position}")
            return
        }
        npc.region = region
        region.addNpc(npc)
    }
}