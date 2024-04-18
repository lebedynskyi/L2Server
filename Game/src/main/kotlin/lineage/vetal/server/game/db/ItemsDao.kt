package lineage.vetal.server.game.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.DBDao
import lineage.vetal.server.game.db.sql.ItemsSQL
import lineage.vetal.server.game.game.model.item.*
import lineage.vetal.server.game.game.model.template.items.ArmorItemTemplate
import lineage.vetal.server.game.game.model.template.items.EtcItemTemplate
import lineage.vetal.server.game.game.model.template.items.ItemTemplate
import lineage.vetal.server.game.game.model.template.items.WeaponItemTemplate

class ItemsDao(
    dbConnection: DBConnection,
    private val itemsTemplates: Map<Int, ItemTemplate>,
) : DBDao(dbConnection) {
    fun saveInventory(items: Set<ItemObject>) {
        items.forEach {item ->
            insertOrUpdate(ItemsSQL.UPDATE_ITEM_OWNER_SQL) { statement ->
                statement.setString(1, item.ownerId)
                statement.setInt(2, item.objectId)
                statement.setInt(3, item.template.id)
                statement.setInt(4, item.count)
                statement.setInt(5, item.enchantLevel)
                statement.setString(6, item.itemLocation.name)
                statement.setInt(7, item.itemLocationData)
                statement.setInt(8, item.customType1)
                statement.setInt(9, item.customType2)
                statement.setInt(10, item.durationLeft)
                statement.setLong(11, item.createTime)
            }
        }
    }

    fun getInventoryForPlayer(playerId: String): List<ItemObject> {
        return queryList(ItemsSQL.SELECT_ITEMS_FOR_PLAYER_SQL, onPrepare = {
            it.setString(1, playerId)
        }) {
            val itemId = it.getInt(3)
            val ownerId = it.getString(1) ?: throw throw IllegalStateException("Unable to find template for item id $itemId")
            val objectId = it.getInt(2)
            val template = itemsTemplates[itemId] ?: throw IllegalStateException("Unable to find template for item id $itemId")

            when (template) {
                is ArmorItemTemplate -> ArmorObject(objectId, ownerId, template)
                is WeaponItemTemplate -> WeaponObject(objectId, ownerId, template)
                is EtcItemTemplate -> EtcItemObject(objectId, ownerId, template)
                else -> throw IllegalStateException("Unable to find template for item id $itemId")
            }.apply {
                count = it.getInt(4)
                enchantLevel = it.getInt(5)
                itemLocation = ItemLocation.valueOf(it.getString(6))
                itemLocationData = it.getInt(7)
                customType1 = it.getInt(8)
                customType2 = it.getInt(9)
                durationLeft = it.getInt(10)
                createTime = it.getLong(11)
            }
        }
    }

    fun getAllObjectIds(): List<Int> {
        return queryList(ItemsSQL.SELECT_ITEMS_IDS_SQL) {
            it.getInt(1)
        }
    }
}