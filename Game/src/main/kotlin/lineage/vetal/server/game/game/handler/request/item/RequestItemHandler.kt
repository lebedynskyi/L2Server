package lineage.vetal.server.game.game.handler.request.item

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.item.usecase.PlayerDropItemUseCase
import lineage.vetal.server.game.game.handler.request.item.usecase.PlayerPickItemUseCase
import lineage.vetal.server.game.game.handler.request.item.usecase.PlayerUseItemUseCase
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.validation.onError
import lineage.vetal.server.game.game.validation.onSuccess
import lineage.vetal.server.game.game.handler.request.item.validation.DropItemValidation
import lineage.vetal.server.game.game.handler.request.item.validation.PickUpValidation
import lineage.vetal.server.game.game.handler.request.item.validation.PickUpValidationError
import lineage.vetal.server.game.game.handler.request.item.validation.UseItemValidation
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.gameserver.packet.server.InventoryList

private const val TAG = "RequestItemHandler"

class RequestItemHandler(
    private val context: GameContext,
    private val dropItemValidation: DropItemValidation = DropItemValidation(),
    private val pickUpValidation: PickUpValidation = PickUpValidation(),
    private val useItemValidation: UseItemValidation = UseItemValidation(),
    private val playerDropItemUseCase: PlayerDropItemUseCase = PlayerDropItemUseCase(),
    private val playerPickItemUseCase: PlayerPickItemUseCase = PlayerPickItemUseCase(),
    private val playerUseItemUseCase: PlayerUseItemUseCase = PlayerUseItemUseCase(),
) {
    fun onPlayerRequestInventory(player: PlayerObject) {
        player.sendPacket(InventoryList(player.inventory.items, false))
    }

    fun onPlayerDropItem(player: PlayerObject, objectId: Int, count: Int, x: Int, y: Int, z: Int) {
        dropItemValidation.validate(player, objectId, count, x, y, z)
            .onSuccess {
                playerDropItemUseCase.onDropItemSuccess(context, player, it, count, x, y, z)
            }.onError {
                playerDropItemUseCase.onDropItemFailed(it, player)
            }
    }

    fun onPlayerPickUpItem(player: PlayerObject, item: ItemObject) {
        pickUpValidation.validate(player, item).onSuccess {
            playerPickItemUseCase.onPlayerPickUpItemSuccess(context, player, item)
        }.onError {
            when (it) {
                is PickUpValidationError.ToFar -> {
                    context.movementManager.startMoveToTask(player, it.targetItem.position, Intention.PICK(it.targetItem))
                }

                else -> {
                    writeDebug(TAG, "Not handle pickup fail with reason -> $it")
                }
            }
        }
    }

    fun onPlayerUseItem(player: PlayerObject, objectId: Int, ctrlPressed: Boolean) {
        useItemValidation.validate(player, objectId, ctrlPressed)
            .onSuccess {
                playerUseItemUseCase.onPlayerUseItemSuccess(context, player, it, ctrlPressed)
            }.onError {
                playerUseItemUseCase.onPlayerUseItemFail(it)
            }
    }

    fun onNpcDropItem() {

    }
}