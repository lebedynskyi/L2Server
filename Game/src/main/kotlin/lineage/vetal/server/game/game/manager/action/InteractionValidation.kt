package lineage.vetal.server.game.game.manager.action

import lineage.vetal.server.game.game.ValidationError
import lineage.vetal.server.game.game.ValidationResult
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.utils.MathUtils

object InteractionValidation {
    fun validate(
        player: PlayerObject,
        actionTarget: CreatureObject?
    ): ValidationResult<CreatureObject, InteractionValidationError> {
        if (actionTarget == null) {
            return ValidationResult.Error(InteractionValidationError.TargetNoExist)
        }
        if (player.isAlikeDead) {
            return ValidationResult.Error(InteractionValidationError.PlayerDead)
        }

        if (!MathUtils.isWithinRadius(player.position, actionTarget.position, 100.0)) {
            return ValidationResult.Error(InteractionValidationError.ToFar(actionTarget))
        }

        return ValidationResult.Success(actionTarget)
    }
}

sealed class InteractionValidationError : ValidationError {
    data object TargetNoExist : InteractionValidationError()
    data class ToFar(val target: CreatureObject) : InteractionValidationError()
    data object PlayerDead : InteractionValidationError()
}