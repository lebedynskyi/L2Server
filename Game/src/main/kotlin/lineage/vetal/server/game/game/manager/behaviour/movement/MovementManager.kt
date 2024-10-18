package lineage.vetal.server.game.game.manager.behaviour.movement

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.manager.behaviour.BehaviourManager
import lineage.vetal.server.game.game.model.behaviour.CreatureBehaviour
import lineage.vetal.server.game.game.model.behaviour.data.MoveData
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.position.Position

class MovementManager(
    private val context: GameContext,
    private val moveToUseCase: BehaviourMoveToUseCase = BehaviourMoveToUseCase()
) : BehaviourManager(context) {

    fun startMoveToTask(creature: CreatureObject, destination: Position, intention: Intention? = null) {
        val moveData = MoveData(destination, context.clock.millis())
        val newIntent = Intention.MOVE_TO(moveData)
        creature.behaviour.setIntention(newIntent, intention)
        manageCreature(creature)
    }

    override fun handleBehaviour(creature: CreatureObject, behaviour: CreatureBehaviour): Boolean {
        val currentIntention = behaviour.current
        return if (currentIntention is Intention.MOVE_TO) {
            moveToUseCase.onBehaviourMoveTo(context, creature, currentIntention)
        } else {
            true
        }
    }
}