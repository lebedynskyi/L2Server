package lineage.vetal.server.game.game.manager.behaviour

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.behaviour.BehaviourResult
import lineage.vetal.server.game.game.model.behaviour.data.MoveData
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.task.ScheduleTask
import lineage.vetal.server.game.game.task.ScheduleTaskManager
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap

class BehaviourManager(
    private val context: GameContext,
    clock: Clock,
    dispatcher: CoroutineDispatcher
) : ScheduleTaskManager(clock, dispatcher) {
    private val activeJobs = ConcurrentHashMap<Int, Job>()

    fun startTask(creature: CreatureObject, intention: Intention, task: BehaviourTask) {
        cancelCurrent(creature)
        creature.behaviour.setAction(intention)
        activeJobs[creature.objectId] = schedule(TaskWrapper(task))
    }

    fun startMovement(creature: CreatureObject, destination: Position, intention: Intention? = null) {
        cancelCurrent(creature)

        val moveData = MoveData(creature.position, destination, context.clock.millis())
        val movement = Intention.MOVE_TO(moveData)
        creature.behaviour.setAction(movement, intention)
        context.movementManager.manageCreature(creature)
    }

    // Single cancellation point: cancels whatever schedule-based or tick-based action
    // was previously running for this creature, regardless of which one it was.
    fun cancelCurrent(creature: CreatureObject) {
        activeJobs.remove(creature.objectId)?.cancel()
        context.movementManager.removeCreature(creature)
    }

    // The only place that advances behaviour.next -> current. Only call on natural completion.
    fun advanceQueue(creature: CreatureObject) {
        // TODO could be any creature
        if (creature.behaviour.endCurrent() && creature is PlayerObject) {
            onPlayerIntention(creature, creature.behaviour.action)
        }
    }

    private fun onPlayerIntention(player: PlayerObject, intention: Intention) {
        when (intention) {
            is Intention.REST -> {}
            is Intention.CAST -> {}
            is Intention.PICK -> context.requestActionHandler.onRequestPickUp(player, intention.data)
            is Intention.ATTACK -> context.requestActionHandler.onRequestAction(player, intention.data.target.objectId)
            is Intention.INTERACT -> context.requestActionHandler.onRequestAction(player, intention.data.target.objectId)
            is Intention.FOLLOW -> {}
            else -> {}
        }
    }

    private inner class TaskWrapper(private val task: BehaviourTask) : ScheduleTask() {
        override suspend fun execute(clock: Clock) {
            when (task.step()) {
                BehaviourResult.IN_PROGRESS -> {
                    activeJobs[task.creature.objectId] = schedule(this, task.nextDelay())
                }

                BehaviourResult.FINISHED -> {
                    activeJobs.remove(task.creature.objectId)
                    advanceQueue(task.creature)
                }

                BehaviourResult.INTERRUPTED -> {
                    activeJobs.remove(task.creature.objectId)
                }
            }
        }
    }
}
