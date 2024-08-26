package lineage.vetal.server.game.game.manager.item.validation

import lineage.vetal.server.game.game.ValidationError
import lineage.vetal.server.game.game.ValidationResult
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.utils.MathUtils

object PickUpValidation {
    fun validate(
        playerObject: CreatureObject,
        itemObject: ItemObject?
    ): ValidationResult<ItemObject, PickUpValidationError> {
        if (itemObject == null) {
            return ValidationResult.Error(PickUpValidationError.NotExist)
        }

        if (!MathUtils.isWithinRadius(playerObject.position, itemObject.position, 100.0)) {
            return ValidationResult.Error(PickUpValidationError.ToFar(itemObject))
        }
        return ValidationResult.Success(itemObject)
    }
}

sealed class PickUpValidationError : ValidationError {
    data object NotExist : PickUpValidationError()
    data class ToFar(val targetItem: ItemObject) : PickUpValidationError()
}