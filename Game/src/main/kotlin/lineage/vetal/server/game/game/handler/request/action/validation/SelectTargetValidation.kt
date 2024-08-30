package lineage.vetal.server.game.game.handler.request.action.validation

import lineage.vetal.server.game.game.validation.Validation
import lineage.vetal.server.game.game.validation.ValidationError
import lineage.vetal.server.game.game.validation.ValidationResult
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject

class SelectTargetValidation : Validation() {
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

sealed interface SelectTargetValidationError : ValidationError {
    data object NotExist: SelectTargetValidationError
    data object PlayerDead: SelectTargetValidationError
}