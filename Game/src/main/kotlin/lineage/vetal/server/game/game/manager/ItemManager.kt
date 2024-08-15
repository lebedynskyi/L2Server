package lineage.vetal.server.game.game.manager

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.item.EtcItemObject
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
        // TODO a lot of conditions here
        // Check delay for action. Introduce some timer ? Protector?
        // TODO store item in the world ?

        if (player.isAlikeDead) {
            return
        }

        // Check is quest and etc?
        val item = player.inventory.getItem(objectId)
        if (item == null) {
            player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
            return
        }

        if (item.count < count) {
            player.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM)
            return
        }

        item.position = SpawnPosition(x, y, z)
        item.ownerId = null

        when (item) {
            is EquipmentObject -> {
                player.inventory.unEquip(item)
            }
        }

        // TODO drop stackable items
        context.gameDatabase.itemsDao.saveItem(item)

        player.inventory.removeItem(item)
        player.region.addItem(item)
        player.region.broadCast(DropItem(item, player.objectId))

        val inventoryUpdate = InventoryUpdate().apply {
            onItemRemoved(item)
        }
        player.sendPacket(inventoryUpdate)
    }

    fun onPlayerPickUpItem(player: PlayerObject, item: ItemObject, x: Int, y: Int, z: Int) {
        // TODO validation of position, owner and etc etc
        // TODO a lot of conditions here. delay and etc. Move to item
        item.ownerId = player.id
        item.position = SpawnPosition.zero

        player.inventory.addItem(item)

        player.region.removeItem(item)
        player.region.broadCast(DeleteObject(item))
        context.gameDatabase.itemsDao.saveItem(item)

        val inventoryUpdate = InventoryUpdate().apply {
            onNewItem(item)
        }
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
                    affectedItems.forEach { onModified(it) }
                })
                context.gameDatabase.itemsDao.saveItems(affectedItems)
            }

            is EtcItemObject -> {
                writeError(TAG, "Use ETC item is not implemented")
            }
        }
    }
}