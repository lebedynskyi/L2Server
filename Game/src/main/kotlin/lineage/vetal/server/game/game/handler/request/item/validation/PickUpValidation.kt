package lineage.vetal.server.game.game.handler.request.item.validation

import lineage.vetal.server.game.game.Error
import lineage.vetal.server.game.game.Result
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.utils.MathUtils

private const val MAX_PICKUP_DISTANCE = 100.0

class PickUpValidation {
    fun validate(
        playerObject: CreatureObject,
        itemObject: ItemObject
    ): Result<ItemObject, PickUpValidationError> {
        if (!MathUtils.isWithinRadius(playerObject.position, itemObject.position, MAX_PICKUP_DISTANCE)) {
            return Result.error(PickUpValidationError.ToFar(itemObject))
        }
        return Result.success(itemObject)
    }
}

sealed interface PickUpValidationError : Error {
    data class ToFar(val targetItem: ItemObject) : PickUpValidationError
}
