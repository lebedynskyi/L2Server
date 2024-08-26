package lineage.vetal.server.game.game.manager.item.validation

import lineage.vetal.server.game.game.Validation
import lineage.vetal.server.game.game.ValidationError
import lineage.vetal.server.game.game.ValidationResult
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.utils.MathUtils

object DropItemValidation : Validation() {
    fun validate(
        player: PlayerObject,
        objectId: Int,
        count: Int,
        x: Int,
        y: Int,
        z: Int
    ): ValidationResult<ItemObject, ValidationError> {
        val item = player.inventory.getItem(objectId)

        if (item == null) {
            return ValidationResult.Error(DropItemValidationError.NoItem)
        }

        if (item.count < count || item.count - count < 0) {
            return ValidationResult.Error(DropItemValidationError.WrongCount)
        }

        if (!item.template.dropable) {
            return ValidationResult.Error(DropItemValidationError.NotDroppable)
        }

        if (item.template.isHeroItem) {
            return ValidationResult.Error(DropItemValidationError.HeroItem)
        }

        if (player.isAlikeDead) {
            return ValidationResult.Error(DropItemValidationError.PlayerAlikeDead)
        }

        if (!MathUtils.isWithinRadius(player.position, Position(x, y, z), 100.0)) {
            return ValidationResult.Error(DropItemValidationError.ToFar)
        }

        return ValidationResult.Success(item)
    }
}

sealed class DropItemValidationError : ValidationError {
    data object NoItem : DropItemValidationError()
    data object WrongCount : DropItemValidationError()
    data object NotDroppable : DropItemValidationError()
    data object HeroItem : DropItemValidationError()
    data object PlayerAlikeDead : DropItemValidationError()
    data object ToFar : DropItemValidationError()
}