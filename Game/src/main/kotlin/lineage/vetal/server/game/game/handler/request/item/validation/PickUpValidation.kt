package lineage.vetal.server.game.game.handler.request.item.validation

import lineage.vetal.server.game.game.validation.Error
import lineage.vetal.server.game.game.validation.Result
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.utils.MathUtils

class PickUpValidation {
    fun validate(
        playerObject: CreatureObject,
        itemObject: ItemObject?
    ): Result<ItemObject, PickUpValidationError> {
        if (itemObject == null) {
            return Result.error(PickUpValidationError.NotExist)
        }

        if (!MathUtils.isWithinRadius(playerObject.position, itemObject.position, 100.0)) {
            return Result.error(PickUpValidationError.ToFar(itemObject))
        }
        return Result.success(itemObject)
    }
}

sealed interface PickUpValidationError : Error {
    data object NotExist : PickUpValidationError
    data class ToFar(val targetItem: ItemObject) : PickUpValidationError
}