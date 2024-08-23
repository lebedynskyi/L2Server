package lineage.vetal.server.game.game.manager.item

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.gameserver.packet.server.DeleteObject
import lineage.vetal.server.game.gameserver.packet.server.InventoryUpdate

object PlayerPickItemUseCase {
    fun onPlayerPickUpItemSuccess(context: GameContext, player: PlayerObject, item: ItemObject, x: Int, y: Int, z: Int) {
        // TODO validation of position, owner and etc etc
        // TODO a lot of conditions here. delay and Move to item
        // TODO if adena sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_S1_ADENA).addNumber(count));
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
        context.worldManager.broadCast(player.region, DeleteObject(item))
        player.sendPacket(inventoryUpdate)
    }
}