package lineage.vetal.server.game.game.handler.request.movement.validation

import lineage.vetal.server.game.game.Error
import lineage.vetal.server.game.game.Result
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position

class MovementValidation {
    fun validate(
        playerObject: PlayerObject,
        destination: Position
    ): Result<List<Position>, MovementValidationValidationError> {
        if (playerObject.stats.getMoveSpeed() <= 0) {
            return Result.error(MovementValidationValidationError.DISABLED)
        }

        if (playerObject.isAlikeDead) {
            return Result.error(MovementValidationValidationError.DEAD)
        }

        // TODO path finding, zones, buildings etc
        return Result.success(listOf(destination))
    }
}

sealed class MovementValidationValidationError : Error {
    data object DISABLED : MovementValidationValidationError()
    data object DEAD : MovementValidationValidationError()
}