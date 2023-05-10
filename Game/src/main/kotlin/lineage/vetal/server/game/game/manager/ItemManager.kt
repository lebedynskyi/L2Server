package lineage.vetal.server.game.game.manager

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.item.EtcItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.server.DropItem
import lineage.vetal.server.game.gameserver.packet.server.InventoryUpdate
import lineage.vetal.server.game.gameserver.packet.server.UserInfo

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

        val item = player.inventory.getItem(objectId) ?: return

        // Check is quest and etc?
        if (item.count < count) {
            return
        }

        item.position = SpawnPosition(x, y, z, 0)
        item.ownerId = null
        // TODO save item ? Save owner ID ?
        player.inventory.removeItem(item)
        player.region.addItem(item)
        player.region.broadCast(DropItem(item, player.objectId))

        val inventoryUpdate = InventoryUpdate()
        inventoryUpdate.onItemRemoved(item)
        player.sendPacket(inventoryUpdate)
    }

    // TODO maybe it is ActionManager ?
    fun onPlayerPickUpItem(player: PlayerObject, objectId: Int, count: Int, x: Int, y: Int, z: Int) {
        // TODO validation of position, owner and etc etc

        // remove from world / region
        // add to inventory
        // Get fresh from inventory with appropriate data
        // send packet
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