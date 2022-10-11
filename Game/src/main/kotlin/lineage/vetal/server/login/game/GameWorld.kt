package lineage.vetal.server.login.game

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.game.model.location.Location
import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.player.Player
import vetal.server.network.SendablePacket
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs

class GameWorld(
    val npc: List<Npc>
) {
    private val TAG = "GameWorld"

    // Geodata min/max tiles
    val TILE_X_MIN = 16
    val TILE_X_MAX = 26
    val TILE_Y_MIN = 10
    val TILE_Y_MAX = 25

    // Map dimensions
    val TILE_SIZE = 32768
    val WORLD_X_MIN = (TILE_X_MIN - 20) * TILE_SIZE
    val WORLD_X_MAX = (TILE_X_MAX - 19) * TILE_SIZE - 1
    val WORLD_Y_MIN = (TILE_Y_MIN - 18) * TILE_SIZE
    val WORLD_Y_MAX = (TILE_Y_MAX - 17) * TILE_SIZE - 1
    val WORLD_Z_MAX = 16410

    // Regions and offsets
    val REGION_SIZE = 2048
    val REGIONS_X = (WORLD_X_MAX - WORLD_X_MIN + 1) / REGION_SIZE
    val REGIONS_Y = (WORLD_Y_MAX - WORLD_Y_MIN + 1) / REGION_SIZE
    val REGION_X_OFFSET = abs(WORLD_X_MIN / REGION_SIZE)
    val REGION_Y_OFFSET = abs(WORLD_Y_MIN / REGION_SIZE)

    val currentOnline get() = _players.size
    private val _players = ConcurrentHashMap<Int, Player>()
    private val _objects = ConcurrentHashMap<Int, GameObject>()
    //    private val _montsers = ConcurrentHashMap<Int, Monster>()

    private val _worldRegions: Array<Array<WorldRegion>> = Array(REGIONS_X) { x ->
        Array(REGIONS_Y) { y ->
            WorldRegion(x, y)
        }
    }

    init {
        for (x in 0 until REGIONS_X) {
            for (y in 0 until REGIONS_Y) {
                for (ix in -1..1) {
                    for (iy in -1..1) {
                        if (isValidRegion(x + ix, y + iy)) {
                            _worldRegions[x + ix][y + iy].addSurroundingRegion(_worldRegions[x][y])
                        }
                    }
                }
            }
        }

        var counter = 0
        npc.forEach {
            val region = getRegion(it.position)
            if (region != null) {
                counter += 1
                region.addNpc(it)
            } else {
                writeInfo(TAG, "Cannot find region for ${it.position} with id ${it.objectId}")
            }
        }
        writeInfo("World", "added $counter npcs to world")


        var mostPopulated: WorldRegion? = null
        _worldRegions.forEach { xR ->
            xR.forEach { yR ->
                if (mostPopulated == null || (mostPopulated?._npc?.size ?: 0) < yR._npc.size) {
                    mostPopulated = yR
                }
            }
        }

        writeInfo("World", "most populated region is  x=${mostPopulated?.tileX} y=${mostPopulated?.tileY} -> $mostPopulated")
    }

    fun spawn(obj: Creature) {
        val region = getRegion(obj.position)
        if (region == null) {
            writeDebug(TAG, "Cannot find region for position ${obj.position}")
            return
        }
        obj.region = region
        when (obj) {
            is Player -> region.addPlayer(obj)
            is Npc -> region.addNpc(obj)
        }
    }

    fun decay(obj: GameObject) {
        val region = getRegion(obj.position)
        if (region == null) {
            writeDebug(TAG, "Cannot find region for position ${obj.position}")
            return
        }
        obj.region = null
        when (obj) {
            is Player -> region.removePlayer(obj)
        }
    }

    fun broadCastPacket(packet: SendablePacket) {
        _players.values.forEach {
            it.sendPacket(packet)
        }
    }

    fun getRegion(loc: Location): WorldRegion? {
        return getRegion(loc.x, loc.y)
    }

    fun getRegion(x: Int, y: Int): WorldRegion {
        return _worldRegions[(x - WORLD_X_MIN) / REGION_SIZE][(y - WORLD_Y_MIN) / REGION_SIZE]
    }

    private fun isValidRegion(x: Int, y: Int): Boolean {
        return (x in 0 until REGIONS_X && y in 0 until REGIONS_Y)
    }
}