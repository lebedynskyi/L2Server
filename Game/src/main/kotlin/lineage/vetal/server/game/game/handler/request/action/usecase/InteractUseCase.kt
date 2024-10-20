package lineage.vetal.server.game.game.handler.request.action.usecase

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.action.validation.InteractionValidationError
import lineage.vetal.server.game.game.model.behaviour.data.AttackData
import lineage.vetal.server.game.game.model.behaviour.data.TargetData
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject

private const val TAG = "InteractFailUseCase"

class InteractUseCase(
    private val context: GameContext
) {
    internal fun onInteractionSuccess(player: PlayerObject, target: CreatureObject) {
        // TODO stop movement.
        // TODO
        if (target.isAutoAttackable) {
            context.attackManager.onCreatureAttack(player, target)
        } else {
            context.interactionManager.onPlayerInteract(player, target)
        }
    }

    internal fun onInteractionError(
        player: PlayerObject,
        reason: InteractionValidationError
    ) {
        when (reason) {
            is InteractionValidationError.ToFar -> {
                val intention = if (reason.target.isAutoAttackable) {
                    Intention.ATTACK(AttackData(reason.target))
                } else {
                    Intention.INTERACT(TargetData(reason.target))
                }

                context.requestMovementHandler.onPlayerStartMovement(player, reason.target.position, intention)
            }

            else -> {
                writeError(TAG, "Not handling onInteractionError with reason $reason")
            }
        }
    }
}