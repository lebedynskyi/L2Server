package lineage.vetal.server.game.game.manager.action

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
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
                is InteractionValidationError.PlayerDead -> TODO()
                is InteractionValidationError.ToFar -> context.movementManager.onPlayerStartMovement(player, reason.target.position)
                else -> {
                    writeError(TAG, "Not handling onInteractionError with reason $reason")
                }
            }
        }

        fun onInteractionSuccess(player: PlayerObject, actionTarget: CreatureObject?) {

        }
    }
}