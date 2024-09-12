package lineage.vetal.server.game.game.handler.request.item.validation

import lineage.vetal.server.game.game.Error
import lineage.vetal.server.game.game.Result
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.utils.MathUtils

class DropItemValidation{
    fun validate(
        player: PlayerObject,
        objectId: Int,
        count: Int,
        x: Int,
        y: Int,
        z: Int
    ): Result<ItemObject, Error> {
        val item = player.inventory.getItem(objectId)

        if (item == null) {
            return Result.error(DropItemValidationError.NoItem)
        }

        if (item.count < count || item.count - count < 0) {
            return Result.error(DropItemValidationError.WrongCount)
        }

        if (!item.template.dropable) {
            return Result.error(DropItemValidationError.NotDroppable)
        }

        if (item.template.isHeroItem) {
            return Result.error(DropItemValidationError.HeroItem)
        }

        if (player.isAlikeDead) {
            return Result.error(DropItemValidationError.PlayerAlikeDead)
        }

        if (!MathUtils.isWithinRadius(player.position, Position(x, y, z), 100.0)) {
            return Result.error(DropItemValidationError.ToFar)
        }

        return Result.success(item)
    }
}

sealed interface DropItemValidationError : Error {
    data object NoItem : DropItemValidationError
    data object WrongCount : DropItemValidationError
    data object NotDroppable : DropItemValidationError
    data object HeroItem : DropItemValidationError
    data object PlayerAlikeDead : DropItemValidationError
    data object ToFar : DropItemValidationError
}