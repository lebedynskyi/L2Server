package lineage.vetal.server.login.game.manager

import lineage.vetal.server.login.game.model.player.PlayerObject
import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.server.DropItem
import lineage.vetal.server.login.gameserver.packet.server.InventoryUpdate

class ItemManager(
    private val worldManager: WorldManager
) {
    fun onPlayerDropItem(client: GameClient, player: PlayerObject, objectId: Int, count: Int, x: Int, y: Int, z: Int) {
        // TODO a lot of conditions here
        // Check delay for action. Introduce some timer ? Protector?
        // TODO store item in the world ?

        if (player.isAlikeDead) {
            return
        }

        val item = player.inventory.getItem(objectId) ?: return

        // check is quest ?
        if (item.count < count) {
            return
        }

        player.inventory.removeItem(item)
        item.position = SpawnPosition(x, y, z, 0)
        item.ownerId

        val inventoryUpdate = InventoryUpdate().apply { this.onItemRemoved(item) }
        player.sendPacket(inventoryUpdate)
        player.region.broadCast(DropItem(item, player.objectId))
    }
}