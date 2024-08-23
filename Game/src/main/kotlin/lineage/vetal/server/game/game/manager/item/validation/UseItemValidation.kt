package lineage.vetal.server.game.game.manager.item.validation

import lineage.vetal.server.game.game.Validation
import lineage.vetal.server.game.game.ValidationError
import lineage.vetal.server.game.game.ValidationResult
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject

object UseItemValidation : Validation() {
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

sealed class UseItemValidationError : ValidationError {
    data object NoItem : UseItemValidationError()
}