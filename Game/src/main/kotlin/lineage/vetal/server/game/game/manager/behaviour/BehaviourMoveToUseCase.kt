package lineage.vetal.server.game.game.manager.behaviour

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.behaviour.data.MoveData
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.utils.MathUtils
import kotlin.math.sqrt

private const val TAG = "BehaviourMoveToUseCase"

object BehaviourMoveToUseCase {
    fun onBehaviourMoveTo(context: GameContext, creature: CreatureObject, intention: Intention.MOVE_TO): Boolean {
        return updatePosition(context, creature, intention.moveData)
    }

    private fun updatePosition(context: GameContext, creature: CreatureObject, movement: MoveData): Boolean {
        val ms = creature.stats.getMoveSpeed()
        val previous = creature.position
        val destination = movement.destination
        // TODO in Lucera originally delta time is 360 millis
        val time = context.clock.millis()
        val current = calculatePosition(previous, destination, ms, (time - movement.lastTime))
        // TODO heading calculation..
        creature.position = SpawnPosition(current.x, current.y, current.z, creature.position.heading)

        if (current == destination) {
            return true
        } else {
            movement.lastTime = time
        }

        // TODO where to put this logic
        if (creature is PlayerObject) {
            context.worldManager.onPlayerPositionChanged(creature, creature.position)
            writeDebug(TAG, "${creature.name} validate server pos=${creature.position}")
//             TODO how often save position ?
//            context.gameDatabase.charactersDao.updateCoordinates(creature.objectId, creature.position)
//             TODO how often validate position ?
//            creature.sendPacket(ValidateLocation(creature))
        }
        return false
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