package lineage.vetal.server.game.game.manager.movement

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.MoveData
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.utils.MathUtils
import lineage.vetal.server.game.gameserver.packet.server.MoveToLocation
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.sqrt

private const val TAG = "MovementManager"

class MovementManager(
    private val context: GameContext
) {
    private val inMovement = ConcurrentHashMap<Int, CreatureObject>()

    fun onPlayerStartMovement(player: PlayerObject, start: Position, finish: Position) {
        startMovement(player, start, finish)
        writeDebug(TAG, "${player.name} move from client [$start] to [$finish], with MS ${player.stats.getMoveSpeed()}")
        writeDebug(TAG, "${player.name} move from server LOC=${player.position}")
        println()
    }

    fun onPlayerValidatePosition(player: PlayerObject, loc: SpawnPosition) {
//        writeDebug(TAG, "${player.name} validate client, MS=${player.stats.getMoveSpeed()}, LOC=$loc")
//        validatePosition(player)
//        writeDebug(TAG, "${player.name} validate server, MS=${player.stats.getMoveSpeed()}, LOC=${player.position}")
//        println()
    }

    fun startMovement(creature: CreatureObject, start: Position, finish: Position) {
        creature.position = SpawnPosition(start.x, start.y, start.z, creature.position.heading) // todo temp
        creature.moveData = MoveData(finish, context.clock.millis())
        context.worldManager.broadCast(creature.region, MoveToLocation(creature, finish))
        inMovement[creature.objectId] = creature
    }

    fun stopMovement(creature: CreatureObject) {
        inMovement.remove(creature.objectId)
        creature.moveData = null
    }

    fun updatePositions() {
        for (entry in inMovement.entries) {
            val creature = entry.value
            updatePosition(creature)
        }
    }

    private fun updatePosition(creature: CreatureObject) {
        val movement = creature.moveData
        if (movement == null) {
            writeError(TAG, "No move data for creature ${creature.name}")
            stopMovement(creature)
            return
        }

        val ms = creature.stats.getMoveSpeed()
        val time = context.clock.millis()
        val previous = creature.position
        val destination = movement.destination
        // TODO in Lucera originally delta time is 360 millis
        val current = calculatePosition(previous, destination, ms, (time - movement.lastTime))
        // TODO heading calculation..
        creature.position = SpawnPosition(current.x, current.y, current.z, creature.position.heading)

        if (current == destination) {
            stopMovement(creature)
        } else {
            movement.lastTime = time
        }

        if (creature is PlayerObject) {
            context.worldManager.onPlayerPositionChanged(creature, creature.position)
            writeDebug(TAG, "${creature.name} validate server pos=${creature.position}")
//             TODO how often save position ?
//            context.gameDatabase.charactersDao.updateCoordinates(creature.objectId, creature.position)
//             TODO how often validate position ?
//            creature.sendPacket(ValidateLocation(creature))
        }
    }

    private fun calculatePosition(
        current: Position,
        destination: Position,
        speed: Double,
        deltaTimeMillis: Long
    ): Position {
        val dx: Int = (destination.x - current.x)
        val dy: Int = (destination.y - current.y)
        val dz: Int = (destination.z - current.z)

        var delta: Double = (dx * dx + dy * dy) * 1.0

        // close enough, allows error between client and server geodata if it cannot be avoided
        // should not be applied on vertical movements in water or during flight. Add check for water and
        delta = if (delta < 10000 && (dz * dz > 2500)) sqrt(delta) else sqrt(delta + dz * dz)

        var distFraction = Double.MAX_VALUE
        if (delta > 1) {
            val distPassed: Double = (speed * deltaTimeMillis) / 1000
            distFraction = distPassed / delta
        }

        // already there, Set the position of the Creature to the destination
        if (distFraction > 1) {
            return destination
        }

        val newPos = Position(
            current.x + ((dx * distFraction)).toInt(),
            current.y + ((dy * distFraction)).toInt(),
            current.z + ((dz * distFraction) + 0.5).toInt()
        )

        return if (MathUtils.isWithinRadius(current, destination, 10.0)) {
            destination
        } else if (MathUtils.hasExceededDestination(current, destination, newPos)) {
            destination
        } else {
            newPos
        }
    }
}