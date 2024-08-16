package lineage.vetal.server.game.game.manager

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.item.EtcItemObject
import lineage.vetal.server.game.game.model.item.ItemLocation
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.server.*

private const val TAG = "ItemManager"

class ItemManager(
    private val context: GameContext
) {
    fun onPlayerDropItem(player: PlayerObject, objectId: Int, count: Int, x: Int, y: Int, z: Int) {
        // Check delay for action. Introduce some timer ? Protector?

        var item = player.inventory.getItem(objectId)
        if (item == null) {
            player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
            return
        }

        if (item.count < count) {
            player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
            return
        }

        if (item.count - count < 0) {
            player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
            return
        }

        if (!item.template.dropable) {
            player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
            return
        }

        if (item.template.isHeroItem) {
            player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
            return
        }

        if (player.isAlikeDead) {
            return
        }

        val inventoryPacket = InventoryUpdate()

        if (item is EtcItemObject) {
            if (item.template.isQuest) {
                player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
                return
            }

            // TODO if adena sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_S1_ADENA).addNumber(count));
            if (item.template.stackable) {
                val newCount = item.count - count
                if (newCount == 0){
                    player.inventory.removeItem(item)
                    inventoryPacket.onRemoved(item)
                } else {
                    player.inventory.updateCount(item.objectId, newCount)
                    context.gameDatabase.itemsDao.saveItem(item)
                    inventoryPacket.onChanged(item)
                    item = context.objectFactory.createItemObject(item.template.id, count)
                }
            } else {
                player.inventory.removeItem(item)
                inventoryPacket.onRemoved(item)
            }
        } else if (item is EquipmentObject) {
            player.inventory.unEquip(item)
            player.inventory.removeItem(item)
            inventoryPacket.onRemoved(item)

        } else {
            player.inventory.removeItem(item)
            inventoryPacket.onRemoved(item)
        }

        item.position = SpawnPosition(x, y, z)
        item.itemLocation = ItemLocation.None
        item.ownerId = null
        player.sendPacket(inventoryPacket)
        player.region.addItem(item)
        player.region.broadCast(DropItem(item, player.objectId))
        context.gameDatabase.itemsDao.saveItem(item)
    }

    fun onPlayerPickUpItem(player: PlayerObject, item: ItemObject, x: Int, y: Int, z: Int) {
        // TODO validation of position, owner and etc etc
        // TODO a lot of conditions here. delay and Move to item
        item.ownerId = player.id
        item.position = SpawnPosition.zero

        val inventoryUpdate = InventoryUpdate()
        val existItem = player.inventory.getItemById(item.template.id)
        if (existItem?.template?.stackable == true && item.template.stackable) {
            player.inventory.updateCount(existItem.objectId, existItem.count + item.count)
            context.gameDatabase.itemsDao.saveItem(existItem)
            inventoryUpdate.onChanged(existItem)
        } else {
            player.inventory.addItem(item)
            context.gameDatabase.itemsDao.saveItem(item)
            inventoryUpdate.onNewItem(item)
        }

        player.region.removeItem(item)
        player.region.broadCast(DeleteObject(item))
        player.sendPacket(inventoryUpdate)
    }

    fun onNpcDropItem() {

    }

    fun onUseItem(client: GameClient, player: PlayerObject, objectId: Int, ctrlPressed: Boolean) {
        val item = player.inventory.getItem(objectId) ?: return

        when (item) {
            is EquipmentObject -> {
                val affectedItems = mutableListOf<ItemObject>()
                if (item.isEquipped) {
                    player.inventory.unEquip(item)
                } else {
                    affectedItems.addAll(player.inventory.equip(item))
                }
                affectedItems.add(item)

                client.sendPacket(UserInfo(player))
                client.sendPacket(InventoryUpdate().apply {
                    affectedItems.forEach { onChanged(it) }
                })
                context.gameDatabase.itemsDao.saveItems(affectedItems)
            }

            is EtcItemObject -> {
                writeError(TAG, "Use ETC item is not implemented")
            }
        }
    }
}