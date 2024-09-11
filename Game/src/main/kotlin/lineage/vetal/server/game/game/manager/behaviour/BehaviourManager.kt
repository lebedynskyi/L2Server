package lineage.vetal.server.game.game.manager.behaviour

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.behaviour.CreatureBehaviour
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.task.TickTask
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "BehaviourManager"

abstract class BehaviourManager(
    private val context: GameContext,
) : TickTask() {
    private val activeCreatures: ConcurrentHashMap<Int, CreatureObject> = ConcurrentHashMap<Int, CreatureObject>()

    protected abstract fun handleBehaviour(creature: CreatureObject, behaviour: CreatureBehaviour): Boolean

    override suspend fun onTick(clock: Clock) {
        activeCreatures.forEach { (key, creature) ->
            val behaviour = creature.behaviour
            if (handleBehaviour(creature, behaviour)) {
                activeCreatures.remove(key)

                if (behaviour.endCurrent()) {
                    onPlayerIntention(creature as PlayerObject, behaviour.current)
                }
            }
        }
    }

    protected fun manageCreature(creature: CreatureObject) {
        activeCreatures[creature.objectId] = creature
    }

    protected fun removeCreature(creature: CreatureObject) {
        activeCreatures.remove(creature.objectId)
    }

    private fun onPlayerIntention(player: PlayerObject, intention: Intention) {
        when (intention) {
            is Intention.REST -> {}
            is Intention.CAST -> {}
            is Intention.PICK -> context.requestItemHandler.onPlayerPickUpItem(player, intention.data)
            is Intention.ATTACK -> context.requestActionHandler.onPlayerAction(player, intention.data.target.objectId)
            is Intention.INTERACT -> context.requestActionHandler.onPlayerAction(player, intention.data.target.objectId)
            is Intention.FOLLOW -> {}
            else -> {}
        }
    }
}
