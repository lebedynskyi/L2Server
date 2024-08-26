package lineage.vetal.server.game.game.manager.behaviour

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
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
    private val context: GameContext
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
        MovementValidation.validate(player, destination)
            .onSuccess {
                startMoveToTask(player, destination, intention)
                context.worldManager.broadCast(player.region, MoveToLocation(player, destination))
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
            is Intention.MOVE_TO -> BehaviourMoveToUseCase.onBehaviourMoveTo(context, creature, currentIntention)
            is Intention.CAST -> false
            // TODO cast workround
            is Intention.PICK -> BehaviourPickUseCase.onBehaviourPick(context, creature as PlayerObject, currentIntention)
            is Intention.ATTACK -> BehaviourAttackUseCase.onBehaviourAttack(context, creature, currentIntention)
            is Intention.FOLLOW -> false
            is Intention.INTERACT -> false
            is Intention.MOVE_TO_IN_A_BOAT -> false
            else -> false
        }
    }
}