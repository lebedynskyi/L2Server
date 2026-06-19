package lineage.vetal.server.game.game.handler.request.item

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.item.usecase.PlayerDropItemUseCase
import lineage.vetal.server.game.game.handler.request.item.usecase.PlayerUseItemUseCase
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onValid
import lineage.vetal.server.game.game.handler.request.item.validation.DropItemValidation
import lineage.vetal.server.game.game.handler.request.item.validation.UseItemValidation
import lineage.vetal.server.game.gameserver.packet.server.InventoryList

class RequestInventoryHandler(
    private val context: GameContext,
    private val dropItemValidation: DropItemValidation = DropItemValidation(),
    private val useItemValidation: UseItemValidation = UseItemValidation(),
    private val playerDropItemUseCase: PlayerDropItemUseCase = PlayerDropItemUseCase(),
    private val playerUseItemUseCase: PlayerUseItemUseCase = PlayerUseItemUseCase(),
) {
    fun onRequestInventoryList(player: PlayerObject) {
        player.sendPacket(InventoryList(player.inventory.items, false))
    }

    fun onRequestDropItem(player: PlayerObject, objectId: Int, count: Int, x: Int, y: Int, z: Int) {
        dropItemValidation.validate(player, objectId, count, x, y, z)
            .onValid {
                playerDropItemUseCase.onDropItemSuccess(context, player, it, count, x, y, z)
            }.onError {
                playerDropItemUseCase.onDropItemFailed(it, player)
            }
    }

    fun onRequestUseItem(player: PlayerObject, objectId: Int, ctrlPressed: Boolean) {
        useItemValidation.validate(player, objectId, ctrlPressed)
            .onValid {
                playerUseItemUseCase.onPlayerUseItemSuccess(context, player, it, ctrlPressed)
            }.onError {
                playerUseItemUseCase.onPlayerUseItemFail(it)
            }
    }
}
