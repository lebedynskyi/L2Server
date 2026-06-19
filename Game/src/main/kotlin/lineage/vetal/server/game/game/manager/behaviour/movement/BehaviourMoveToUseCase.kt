package lineage.vetal.server.game.game.manager.behaviour.movement

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.behaviour.data.MoveData
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.gameserver.packet.server.ValidateLocation
import kotlin.math.roundToInt

private const val TAG = "BehaviourMoveToUseCase"

class BehaviourMoveToUseCase {
    fun onBehaviourMoveTo(context: GameContext, creature: CreatureObject, intention: Intention.MOVE_TO): Boolean {
        return updatePosition(context, creature, intention.data)
    }

    private fun updatePosition(context: GameContext, creature: CreatureObject, movement: MoveData): Boolean {
        val time = context.clock.millis()
        val arrived = isArrived(creature, movement, time)
        creature.position = calculatePosition(creature, movement, time, arrived)

        // TODO where to put this logic
        if (creature is PlayerObject) {
            context.gameWorld.onPlayerPositionChanged(creature, creature.position)
            writeDebug(TAG, "${creature.name} Server pos=${creature.position}")
            // TODO how often save position ?
            // context.gameDatabase.charactersDao.updateCoordinates(creature.objectId, creature.position)
        }

        // Re-sync everyone (including the moving client) to the authoritative position once the
        // move is finished, so any client side prediction drift collapses without rubber-banding
        // the player mid-move.
        if (arrived) {
            context.broadcaster.broadCast(creature.region, ValidateLocation(creature))
        }
        return arrived
    }

    private fun isArrived(creature: CreatureObject, movement: MoveData, time: Long): Boolean {
        if (movement.totalDistance <= 0.0) {
            return true
        }
        val speed = creature.stats.getMoveSpeed()
        val distancePassed = speed * (time - movement.moveStartTime) / 1000.0
        return distancePassed >= movement.totalDistance
    }

    /**
     * Computes the absolute position by interpolating from the fixed [MoveData.origin] using the
     * total elapsed time since the move started. Because it always interpolates from the unchanging
     * origin, sub-unit rounding is applied fresh each tick and never accumulates into drift, so the
     * server keeps pace with the client instead of slowly falling behind.
     */
    private fun calculatePosition(
        creature: CreatureObject,
        movement: MoveData,
        time: Long,
        arrived: Boolean
    ): SpawnPosition {
        val destination = movement.destination
        if (arrived) {
            return SpawnPosition(destination.x, destination.y, destination.z, movement.heading)
        }

        val origin = movement.origin
        val speed = creature.stats.getMoveSpeed()
        val distancePassed = speed * (time - movement.moveStartTime) / 1000.0
        val fraction = distancePassed / movement.totalDistance

        return SpawnPosition(
            origin.x + ((destination.x - origin.x) * fraction).roundToInt(),
            origin.y + ((destination.y - origin.y) * fraction).roundToInt(),
            origin.z + ((destination.z - origin.z) * fraction).roundToInt(),
            movement.heading
        )
    }
}
