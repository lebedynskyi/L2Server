package lineage.vetal.server.game.game.handler.request.world.validation

import lineage.vetal.server.game.game.validation.ValidationError
import lineage.vetal.server.game.game.validation.ValidationResult
import lineage.vetal.server.game.game.model.player.PlayerObject

class RestartValidation {
    fun validate(playerObject: PlayerObject): ValidationResult<Boolean, RestartValidationError> {
        if (playerObject.isInCombat) {
            return ValidationResult.Error(RestartValidationError.InCombat)
        }

        return ValidationResult.Success(true)
    }
}

sealed interface RestartValidationError: ValidationError {
    data object InCombat: RestartValidationError
}
