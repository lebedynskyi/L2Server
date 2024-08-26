package lineage.vetal.server.game.game.manager.action

import lineage.vetal.server.game.game.Validation
import lineage.vetal.server.game.game.ValidationError
import lineage.vetal.server.game.game.ValidationResult
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject


object SelectTargetValidation : Validation() {
    fun validate(player: PlayerObject, requestedTarget: CreatureObject?): ValidationResult<CreatureObject, SelectTargetValidationError> {
        if (requestedTarget == null) {
            return ValidationResult.Error(SelectTargetValidationError.NotExist)
        }

        if (player.isAlikeDead) {
            return ValidationResult.Error(SelectTargetValidationError.PlayerDead)
        }

        return ValidationResult.Success(requestedTarget)
    }
}

sealed class SelectTargetValidationError : ValidationError {
    data object NotExist: SelectTargetValidationError()
    data object PlayerDead: SelectTargetValidationError()
}