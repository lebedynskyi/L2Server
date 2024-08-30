package lineage.vetal.server.game.game.handler.request.item.validation

import lineage.vetal.server.game.game.validation.Validation
import lineage.vetal.server.game.game.validation.ValidationError
import lineage.vetal.server.game.game.validation.ValidationResult
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject

class UseItemValidation : Validation() {
    fun validate(
        player: PlayerObject,
        objectId: Int,
        ctrlPressed: Boolean
    ): ValidationResult<ItemObject, ValidationError> {
        val item = player.inventory.getItem(objectId)

        if (item == null) {
            return ValidationResult.Error(UseItemValidationError.NoItem)
        }

        return ValidationResult.Success(item)
    }
}

sealed interface UseItemValidationError : ValidationError {
    data object NoItem : UseItemValidationError
}