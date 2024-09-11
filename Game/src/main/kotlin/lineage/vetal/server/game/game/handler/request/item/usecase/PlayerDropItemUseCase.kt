package lineage.vetal.server.game.game.handler.request.item.usecase

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.item.EtcItemObject
import lineage.vetal.server.game.game.model.item.ItemLocation
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.validation.Error
import lineage.vetal.server.game.gameserver.packet.server.*

class PlayerDropItemUseCase {
    internal fun onDropItemSuccess(context: GameContext, player: PlayerObject, item: ItemObject, count: Int, x: Int, y: Int, z: Int) {
        var itemToDrop = item
        val inventoryPacket = InventoryUpdate()

        if (itemToDrop is EtcItemObject) {
            if (itemToDrop.template.isQuest) {
                player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
                return
            }

            if (itemToDrop.template.stackable) {
                val newCount = itemToDrop.count - count
                if (newCount == 0) {
                    player.inventory.removeItem(itemToDrop)
                    inventoryPacket.onRemoved(itemToDrop)
                } else {
                    player.inventory.updateCount(itemToDrop.objectId, newCount)
                    context.gameDatabase.itemsDao.saveItem(itemToDrop)
                    inventoryPacket.onChanged(itemToDrop)
                    itemToDrop = context.objectFactory.createItemObject(itemToDrop.template.id, count)
                }
            } else {
                player.inventory.removeItem(itemToDrop)
                inventoryPacket.onRemoved(itemToDrop)
            }
        } else if (itemToDrop is EquipmentObject) {
            if (itemToDrop.isEquipped) {
                player.inventory.unEquip(itemToDrop)
                context.gameWorld.broadCast(player.region, CharInfo(player))
                player.sendPacket(InventoryList(player.inventory.items, false))
            }
            player.inventory.removeItem(itemToDrop)
            inventoryPacket.onRemoved(itemToDrop)
        } else {
            player.inventory.removeItem(itemToDrop)
            inventoryPacket.onRemoved(itemToDrop)
        }

        itemToDrop.position = SpawnPosition(x, y, z)
        itemToDrop.itemLocation = ItemLocation.None
        itemToDrop.ownerId = null
        player.sendPacket(inventoryPacket)
        player.region.addItem(itemToDrop)
        context.gameWorld.broadCast(player.region, DropItem(itemToDrop, player.objectId))
        context.gameDatabase.itemsDao.saveItem(itemToDrop)
    }

    internal fun onDropItemFailed(reason: Error, player: PlayerObject) {
        player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
    }
}