package lineage.vetal.server.game.game.handler.tick.behaviour

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.tick.behaviour.usecase.BehaviourAttackUseCase
import lineage.vetal.server.game.game.handler.tick.behaviour.usecase.BehaviourMoveToUseCase
import lineage.vetal.server.game.game.handler.tick.behaviour.usecase.BehaviourPickUseCase
import lineage.vetal.server.game.game.handler.tick.behaviour.validation.BehaviourMoveToValidation
import lineage.vetal.server.game.game.model.behaviour.CreatureBehaviour
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.behaviour.data.MoveData
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onSuccess
import lineage.vetal.server.game.gameserver.packet.server.MoveToLocation
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "BehaviourManager"

class BehaviourManager(
    private val context: GameContext,
    private val behaviourAttackUseCase: BehaviourAttackUseCase = BehaviourAttackUseCase(),
    private val behaviourMoveToUseCase: BehaviourMoveToUseCase = BehaviourMoveToUseCase(),
    private val behaviourMoveToValidation: BehaviourMoveToValidation = BehaviourMoveToValidation(),
    private val behaviourPickUseCase: BehaviourPickUseCase = BehaviourPickUseCase(),
) {
    private val activeCreatures: ConcurrentHashMap<Int, CreatureObject> = ConcurrentHashMap<Int, CreatureObject>()

    fun onTick() {
        activeCreatures.forEach { (key, creature) ->
            val behaviour = creature.behaviour
            if (handleBehaviour(creature, behaviour) && !behaviour.endCurrent()) {
                activeCreatures.remove(key)
            }
        }
    }

    fun onPlayerStartMovement(player: PlayerObject, destination: Position, intention: Intention? = null) {
        behaviourMoveToValidation.validate(player, destination)
            .onSuccess {
                startMoveToTask(player, destination, intention)
                context.gameWorld.broadCast(player.region, MoveToLocation(player, destination))
            }.onError {
                writeDebug(TAG, "Start move error -> $it")
            }
    }

    fun onPlayerIntention(player: PlayerObject, intention: Intention) {
        player.behaviour.setIntention(intention)
        activeCreatures[player.objectId] = player
    }

    private fun startMoveToTask(creature: CreatureObject, destination: Position, intention: Intention? = null) {
        // TODO validate. Stop current?
        val moveData = MoveData(destination, context.clock.millis())
        val newIntent = Intention.MOVE_TO(moveData)
        creature.behaviour.setIntention(newIntent, intention)
        activeCreatures[creature.objectId] = creature
    }

    private fun handleBehaviour(creature: CreatureObject, behaviour: CreatureBehaviour): Boolean {
        return when (val currentIntention = behaviour.current) {
            is Intention.ACTIVE -> false
            is Intention.REST -> false
            is Intention.MOVE_TO -> behaviourMoveToUseCase.onBehaviourMoveTo(context, creature, currentIntention)
            is Intention.CAST -> false
            // TODO cast workround. Neeed investigate somehow
            is Intention.PICK -> behaviourPickUseCase.onBehaviourPick(context, creature as PlayerObject, currentIntention)
            is Intention.ATTACK -> behaviourAttackUseCase.onBehaviourAttack(context, creature, currentIntention)
            is Intention.FOLLOW -> false
            is Intention.INTERACT -> false
            is Intention.MOVE_TO_IN_A_BOAT -> false
            else -> false
        }
    }
}