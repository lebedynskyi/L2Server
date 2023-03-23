package lineage.vetal.server.login.game.model

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.game.model.location.Location
import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.player.Player
import vetal.server.network.SendablePacket
import java.util.concurrent.ConcurrentHashMap

// Geodata min/max tiles
private const val GEO_TILE_X_MIN = 16
private const val GEO_TILE_X_MAX = 26
private const val GEO_TILE_Y_MIN = 10
private const val GEO_TILE_Y_MAX = 25

// Map dimensions
private const val WORLD_TILE_SIZE = 32768
private const val WORLD_X_MIN = (GEO_TILE_X_MIN - 20) * WORLD_TILE_SIZE
private const val WORLD_X_MAX = (GEO_TILE_X_MAX - 19) * WORLD_TILE_SIZE - 1
private const val WORLD_Y_MIN = (GEO_TILE_Y_MIN - 18) * WORLD_TILE_SIZE
private const val WORLD_Y_MAX = (GEO_TILE_Y_MAX - 17) * WORLD_TILE_SIZE - 1
val WORLD_Z_MAX = 16410

// Regions and offsets
private const val REGION_SIZE = 2048
private const val REGIONS_X = (WORLD_X_MAX - WORLD_X_MIN + 1) / REGION_SIZE
private const val REGIONS_Y = (WORLD_Y_MAX - WORLD_Y_MIN + 1) / REGION_SIZE
//private const val REGION_X_OFFSET = abs(WORLD_X_MIN / REGION_SIZE)
//private const val REGION_Y_OFFSET = abs(WORLD_Y_MIN / REGION_SIZE)

//class GameWorld(
//    val npc: List<Npc>
//) {
//    private val TAG = "GameWorld"
//
//    val currentOnline get() = _players.size
//    private val _players = ConcurrentHashMap<Int, Player>()
////    private val _objects = ConcurrentHashMap<Int, GameObject>()
////    private val _montsers = ConcurrentHashMap<Int, Monster>()
//
//    private val _worldRegionOlds: Array<Array<WorldRegionOld>> =
//        Array(REGIONS_X) { x ->
//            Array(REGIONS_Y) { y ->
//                WorldRegionOld(x, y)
//            }
//        }
//
//    init {
//        for (x in 0 until REGIONS_X) {
//            for (y in 0 until REGIONS_Y) {
//                for (ix in -1..1) {
//                    for (iy in -1..1) {
//                        if (isValidRegion(x + ix, y + iy)) {
//                            _worldRegionOlds[x + ix][y + iy].addSurroundingRegion(_worldRegionOlds[x][y])
//                        }
//                    }
//                }
//            }
//        }
//
//        var counter = 0
//        npc.forEach {
//            val region = getRegion(it.position)
//            if (region != null) {
//                counter += 1
//                region.addNpc(it)
//            } else {
//                writeInfo(TAG, "Cannot find region for ${it.position} with id ${it.objectId}")
//            }
//        }
//        writeInfo("World", "added $counter npcs to world")
//    }
//
//    fun spawn(creature: Creature) {
//        val region = getRegion(creature.position)
//        if (region == null) {
//            writeDebug(TAG, "Cannot find region for position ${creature.position}")
//            return
//        }
//        creature.region = region
//        when (creature) {
//            is Player -> {
//                region.addPlayer(creature)
//                _players[creature.objectId] = creature
//            }
//            is Npc -> region.addNpc(creature)
//        }
//    }
//
//    fun decay(obj: GameObject) {
//        val region = getRegion(obj.position)
//        if (region == null) {
//            writeDebug(TAG, "Cannot find region for position ${obj.position}")
//            return
//        }
//        obj.region = null
//        when (obj) {
//            is Player -> region.removePlayer(obj)
//        }
//    }
//
//    fun broadCastPacket(packet: SendablePacket) {
//        _players.values.forEach {
//            it.sendPacket(packet)
//        }
//    }
//
//    fun getRegion(loc: Location): WorldRegionOld? {
//        return getRegion(loc.x, loc.y)
//    }
//
//    fun getRegion(x: Int, y: Int): WorldRegionOld {
//        return _worldRegionOlds[(x - WORLD_X_MIN) / REGION_SIZE][(y - WORLD_Y_MIN) / REGION_SIZE]
//    }
//
//    private fun isValidRegion(x: Int, y: Int): Boolean {
//        return (x in 0 until REGIONS_X && y in 0 until REGIONS_Y)
//    }
//}