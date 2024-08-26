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

class InteractFailUseCase {
    companion object {
        fun onInteractionError(
            context: GameContext,
            player: PlayerObject,
            reason: InteractionValidationError
        ) {
            when (reason) {
                is InteractionValidationError.PlayerDead -> {}
                is InteractionValidationError.ToFar -> {
                    val intention = if (reason.target.isAutoAttackable) {
                        Intention.ATTACK(AttackData(reason.target))
                    } else {
                        Intention.INTERACT(TargetData(reason.target))
                    }

                    context.behaviourManager.onPlayerStartMovement(player, reason.target.position, intention)
                }

                else -> {
                    writeError(TAG, "Not handling onInteractionError with reason $reason")
                }
            }
        }

        fun onInteractionSuccess(context: GameContext, player: PlayerObject, actionTarget: CreatureObject) {
            val intention = if (actionTarget.isAutoAttackable) {
                Intention.ATTACK(AttackData(actionTarget, context.clock.millis()))
            } else {
                Intention.INTERACT(TargetData(actionTarget, context.clock.millis()))
            }

            context.behaviourManager.onPlayerIntention(player, intention)
        }
    }
}