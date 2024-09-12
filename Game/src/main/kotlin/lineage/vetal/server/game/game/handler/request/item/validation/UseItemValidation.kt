package lineage.vetal.server.game.game.handler.request.item.validation

import lineage.vetal.server.game.game.Error
import lineage.vetal.server.game.game.Result
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject

class UseItemValidation {
    fun validate(
        player: PlayerObject,
        objectId: Int,
        ctrlPressed: Boolean
    ): Result<ItemObject, Error> {
        val item = player.inventory.getItem(objectId)

        if (item == null) {
            return Result.error(UseItemValidationError.NoItem)
        }

        return Result.success(item)
    }
}

sealed interface UseItemValidationError : Error {
    data object NoItem : UseItemValidationError
}