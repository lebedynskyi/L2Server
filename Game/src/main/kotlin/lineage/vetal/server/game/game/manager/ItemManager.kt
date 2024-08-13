package lineage.vetal.server.game.game.manager

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.item.EtcItemObject
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.server.*

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
                item.equippedSlot = 0
            }
        }

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
                item.equippedSlot = item.template.bodySlot
                // Update inventory
                player.sendPacket(UserInfo(player))
                player.sendPacket(InventoryUpdate().apply {
                    onModified(item)
                })
            }

            is EtcItemObject -> {
                System.err.println("XXX: Use EtcItem")
            }
        }
    }
}