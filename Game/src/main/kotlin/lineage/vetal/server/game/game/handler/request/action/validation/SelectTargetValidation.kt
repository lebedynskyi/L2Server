package lineage.vetal.server.game.game.handler.request.action.validation

import lineage.vetal.server.game.game.Error
import lineage.vetal.server.game.game.Result
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject

class SelectTargetValidation {
    fun validate(player: PlayerObject, requestedTarget: CreatureObject?): Result<CreatureObject, SelectTargetValidationError> {
        if (requestedTarget == null) {
            return Result.error(SelectTargetValidationError.NotExist)
        }

        if (player.isAlikeDead) {
            return Result.error(SelectTargetValidationError.PlayerDead)
        }

        return Result.success(requestedTarget)
    }
}

sealed interface SelectTargetValidationError : Error {
    data object NotExist: SelectTargetValidationError
    data object PlayerDead: SelectTargetValidationError
}