package lineage.vetal.server.game.game.manager.movement

import lineage.vetal.server.game.game.Validation
import lineage.vetal.server.game.game.ValidationError
import lineage.vetal.server.game.game.ValidationResult
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position

class MovementValidation : Validation() {
    fun validate(
        playerObject: PlayerObject,
        destination: Position
    ): ValidationResult<List<Position>, MovementValidationError> {
        if (playerObject.stats.getMoveSpeed() <= 0) {
            return ValidationResult.Error(MovementValidationError.DISABLED)
        }

        // TODO path finding, zones, buildings etc
        return ValidationResult.Success(listOf(destination))
    }
}


sealed class MovementValidationError : ValidationError {
    data object DISABLED : MovementValidationError()
}