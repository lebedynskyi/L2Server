package lineage.vetal.server.game.game.manager.item

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onSuccess
import lineage.vetal.server.game.game.manager.item.validation.DropItemValidation
import lineage.vetal.server.game.game.manager.item.validation.UseItemValidation

class ItemManager(
    private val context: GameContext
) {
    fun onPlayerDropItem(player: PlayerObject, objectId: Int, count: Int, x: Int, y: Int, z: Int) {
        DropItemValidation.validate(player, objectId, count, x, y, z)
            .onSuccess {
                PlayerDropItemUseCase.onDropItemSuccess(context, player, it, count, x, y, z)
            }.onError {
                PlayerDropItemUseCase.onDropItemFailed(it, player)
            }
    }

    fun onPlayerPickUpItem(player: PlayerObject, item: ItemObject, x: Int, y: Int, z: Int) {
        PlayerPickItemUseCase.onPlayerPickUpItemSuccess(context, player, item, x, y, z)
    }

    fun onPlayerUseItem(player: PlayerObject, objectId: Int, ctrlPressed: Boolean) {
        UseItemValidation.validate(player, objectId, ctrlPressed).onSuccess {
            PlayerUseItemUseCase.onPlayerUseItemSuccess(context, player, it, ctrlPressed)
        }.onError {
            PlayerUseItemUseCase.onPlayerUseIteFail(it)
        }
    }

    fun onNpcDropItem() {

    }
}