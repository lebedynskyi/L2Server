package lineage.vetal.server.game.game.handler.request.item.usecase

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.Error
import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.item.EtcItemObject
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.server.InventoryUpdate
import lineage.vetal.server.game.gameserver.packet.server.UserInfo

private const val TAG = "PlayerUseItemUseCase"

class PlayerUseItemUseCase {
    internal fun onPlayerUseItemSuccess(context: GameContext, player: PlayerObject, item: ItemObject, ctrlPressed: Boolean) {
        when (item) {
            is EquipmentObject -> {
                val affectedItems = mutableListOf<ItemObject>()
                if (item.isEquipped) {
                    player.inventory.unEquip(item)
                } else {
                    affectedItems.addAll(player.inventory.equip(item))
                }
                affectedItems.add(item)

                context.gameWorld.broadCast(player.region, UserInfo(player))
                player.sendPacket(InventoryUpdate().apply {
                    affectedItems.forEach { onChanged(it) }
                })
                context.gameDatabase.itemsDao.saveItems(affectedItems)
            }

            is EtcItemObject -> {
                writeError(TAG, "Use ETC item is not implemented")
            }
        }
    }

    internal fun onPlayerUseItemFail(reason: Error) {

    }
}