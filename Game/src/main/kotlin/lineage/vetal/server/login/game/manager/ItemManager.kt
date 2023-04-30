package lineage.vetal.server.login.game.manager

import lineage.vetal.server.login.game.model.player.PlayerObject
import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.gameserver.packet.server.DropItem
import lineage.vetal.server.login.gameserver.packet.server.InventoryUpdate

class ItemManager(
    private val worldManager: WorldManager
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
}