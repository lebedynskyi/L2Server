package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.Dao
import lineage.vetal.server.login.game.model.item.ItemObject
import lineage.vetal.server.login.game.model.template.items.ItemTemplate

class ItemsDao(
    dbConnection: DBConnection,
    val itemsTemplates: Map<Int, ItemTemplate>,
) : Dao(dbConnection) {
    fun saveInventory(items: Set<ItemObject>) {
        items.forEach {

        }
    }

    fun getInventoryForPlayer(playerId: String): List<ItemObject> {
        return emptyList()
    }
}