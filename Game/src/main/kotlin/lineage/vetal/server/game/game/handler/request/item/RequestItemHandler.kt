package lineage.vetal.server.game.game.handler.request.item

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.item.usecase.PlayerDropItemUseCase
import lineage.vetal.server.game.game.handler.request.item.usecase.PlayerPickItemUseCase
import lineage.vetal.server.game.game.handler.request.item.usecase.PlayerUseItemUseCase
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onSuccess
import lineage.vetal.server.game.game.handler.request.item.validation.DropItemValidation
import lineage.vetal.server.game.game.handler.request.item.validation.PickUpValidation
import lineage.vetal.server.game.game.handler.request.item.validation.PickUpValidationError
import lineage.vetal.server.game.game.handler.request.item.validation.UseItemValidation
import lineage.vetal.server.game.game.model.intenttion.Intention

private const val TAG = "ItemManager"

class RequestItemHandler(
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

    fun onPlayerPickUpItem(player: PlayerObject, item: ItemObject) {
        PickUpValidation.validate(player, item).onSuccess {
            PlayerPickItemUseCase.onPlayerPickUpItemSuccess(context, player, item)
        }.onError {
            when (it) {
                is PickUpValidationError.ToFar -> {
                    context.behaviourManager.onPlayerStartMovement(player, it.targetItem.position, Intention.PICK(it.targetItem))
                }

                else -> {
                    writeDebug(TAG, "Not handle pickup fail with reason -> $it")
                }
            }
        }
    }

    fun onPlayerUseItem(player: PlayerObject, objectId: Int, ctrlPressed: Boolean) {
        UseItemValidation.validate(player, objectId, ctrlPressed)
            .onSuccess {
                PlayerUseItemUseCase.onPlayerUseItemSuccess(context, player, it, ctrlPressed)
            }.onError {
                PlayerUseItemUseCase.onPlayerUseItemFail(it)
            }
    }

    fun onNpcDropItem() {

    }
}