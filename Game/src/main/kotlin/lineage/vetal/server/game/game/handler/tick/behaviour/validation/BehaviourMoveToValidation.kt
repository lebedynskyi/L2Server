package lineage.vetal.server.game.game.handler.tick.behaviour.validation

import lineage.vetal.server.game.game.Validation
import lineage.vetal.server.game.game.ValidationError
import lineage.vetal.server.game.game.ValidationResult
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position

class BehaviourMoveToValidation : Validation() {
    fun validate(
        playerObject: PlayerObject,
        destination: Position
    ): ValidationResult<List<Position>, BehaviourMoveToValidationError> {
        if (playerObject.stats.getMoveSpeed() <= 0) {
            return ValidationResult.Error(BehaviourMoveToValidationError.DISABLED)
        }

        // TODO path finding, zones, buildings etc
        return ValidationResult.Success(listOf(destination))
    }
}

sealed class BehaviourMoveToValidationError : ValidationError {
    data object DISABLED : BehaviourMoveToValidationError()
}