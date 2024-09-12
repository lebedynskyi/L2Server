package lineage.vetal.server.game.game.handler.request.world.validation

import lineage.vetal.server.game.game.Error
import lineage.vetal.server.game.game.Result
import lineage.vetal.server.game.game.model.player.PlayerObject

class RestartValidation {
    fun validate(player: PlayerObject): Result<PlayerObject, RestartValidationError> {
        if (!player.isInWorld) {
            return Result.error(RestartValidationError.NotInWorld)
        }

        if (player.isInCombat) {
            return Result.error(RestartValidationError.InCombat)
        }

        return Result.success(player)
    }
}

sealed interface RestartValidationError: Error {
    data object InCombat: RestartValidationError
    data object NotInWorld: RestartValidationError
}
