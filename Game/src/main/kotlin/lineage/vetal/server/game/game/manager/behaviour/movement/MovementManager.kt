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
    fun startMovement(creature: CreatureObject, destination: Position, intention: Intention? = null) {
        val origin = Position(creature.position)
        val moveData = MoveData(origin, destination, context.clock.millis())
        val movement = Intention.MOVE_TO(moveData)
        creature.behaviour.setIntention(movement, intention)
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