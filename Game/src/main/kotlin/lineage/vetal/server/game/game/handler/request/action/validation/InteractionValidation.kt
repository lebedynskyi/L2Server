package lineage.vetal.server.game.game.handler.request.action.validation

import lineage.vetal.server.game.game.Error
import lineage.vetal.server.game.game.Result
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.utils.MathUtils

private const val INTERACTION_RANGE = 100.0

class InteractionValidation {
    fun validate(
        player: PlayerObject,
        actionTarget: CreatureObject?
    ): Result<CreatureObject, InteractionValidationError> {
        if (actionTarget == null) {
            return Result.error(InteractionValidationError.TargetNoExist)
        }
        if (player.isAlikeDead) {
            return Result.error(InteractionValidationError.PlayerDead)
        }

        if (!MathUtils.isWithinRadius(player.position, actionTarget.position, INTERACTION_RANGE)) {
            return Result.error(InteractionValidationError.ToFar(actionTarget))
        }

        return Result.success(actionTarget)
    }
}

sealed interface InteractionValidationError : Error {
    data class ToFar(val target: CreatureObject) : InteractionValidationError

    data object TargetNoExist : InteractionValidationError
    data object PlayerDead : InteractionValidationError
}