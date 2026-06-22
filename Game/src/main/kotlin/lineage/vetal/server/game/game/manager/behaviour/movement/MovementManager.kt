package lineage.vetal.server.game.game.manager.behaviour.movement

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.behaviour.BehaviourResult
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.task.TickTask
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap

class MovementManager(
    private val context: GameContext,
    private val moveToUseCase: BehaviourMoveToUseCase = BehaviourMoveToUseCase()
) : TickTask() {
    private val activeCreatures = ConcurrentHashMap<Int, CreatureObject>()

    override suspend fun onTick(clock: Clock) {
        activeCreatures.forEach { (key, creature) ->
            when (handleBehaviour(creature)) {
                BehaviourResult.IN_PROGRESS -> {}

                BehaviourResult.FINISHED -> {
                    activeCreatures.remove(key)
                    context.behaviourManager.advanceQueue(creature)
                }

                BehaviourResult.INTERRUPTED -> {
                    activeCreatures.remove(key)
                }
            }
        }
    }

    internal fun manageCreature(creature: CreatureObject) {
        activeCreatures[creature.objectId] = creature
    }

    internal fun removeCreature(creature: CreatureObject) {
        activeCreatures.remove(creature.objectId)
    }

    private fun handleBehaviour(creature: CreatureObject): BehaviourResult {
        val currentIntention = creature.behaviour.current
        return if (currentIntention !is Intention.MOVE_TO) {
            BehaviourResult.INTERRUPTED
        } else if (moveToUseCase.onBehaviourMoveTo(context, creature, currentIntention)) {
            BehaviourResult.FINISHED
        } else {
            BehaviourResult.IN_PROGRESS
        }
    }
}
