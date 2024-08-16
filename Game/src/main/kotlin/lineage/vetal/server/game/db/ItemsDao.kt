package lineage.vetal.server.game.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.DBDao
import lineage.vetal.server.game.db.sql.ItemsSQL
import lineage.vetal.server.game.game.model.inventory.PaperDollSlot
import lineage.vetal.server.game.game.model.item.*
import lineage.vetal.server.game.game.model.template.items.ArmorItemTemplate
import lineage.vetal.server.game.game.model.template.items.EtcItemTemplate
import lineage.vetal.server.game.game.model.template.items.ItemTemplate
import lineage.vetal.server.game.game.model.template.items.WeaponItemTemplate

class ItemsDao(
    dbConnection: DBConnection,
    private val itemsTemplates: Map<Int, ItemTemplate>,
) : DBDao(dbConnection) {
    fun saveItems(items: Iterable<ItemObject>) {
        items.forEach(::saveItem)
    }

    fun saveItem(item: ItemObject) {
        insertOrUpdate(ItemsSQL.INSERT_OR_UPDATE_SQL) { statement ->
            statement.setString(1, item.ownerId)
            statement.setInt(2, item.objectId)
            statement.setInt(3, item.template.id)
            statement.setInt(4, item.count)
            statement.setInt(5, item.enchantLevel)
            statement.setString(6, item.itemLocation.name)
            statement.setInt(7, item.itemLocation.data)
            statement.setInt(8, item.customType1)
            statement.setInt(9, item.customType2)
            statement.setInt(10, item.durationLeft)
            statement.setLong(11, item.createTime)
        }
    }

    fun getItemsForPlayer(playerId: String): List<ItemObject> {
        return queryList(ItemsSQL.SELECT_ITEMS_FOR_PLAYER_SQL, onPrepare = {
            it.setString(1, playerId)
        }) {
            val itemId = it.getInt(3)
            val ownerId = it.getString(1) ?: throw throw IllegalStateException("Unable to find template for item id $itemId")
            val template = itemsTemplates[itemId] ?: throw IllegalStateException("Unable to find template for item id $itemId")
            val objectId = it.getInt(2)

            when (template) {
                is ArmorItemTemplate -> ArmorObject(objectId, template)
                is WeaponItemTemplate -> WeaponObject(objectId, template)
                is EtcItemTemplate -> EtcItemObject(objectId, template)
                else -> throw IllegalStateException("Unable to find template for item id $itemId")
            }.apply {
                this.ownerId = ownerId
                count = it.getInt(4)
                enchantLevel = it.getInt(5)
                itemLocation = ItemLocation.from(it.getString(6), it.getInt(7))
                customType1 = it.getInt(8)
                customType2 = it.getInt(9)
                durationLeft = it.getInt(10)
                createTime = it.getLong(11)
            }
        }
    }

    fun getVisibleItemsForPlayer(playerId: String): List<ItemObject> {
        return queryList(ItemsSQL.SELECT_VISIBLE_ITEMS_FOR_PLAYER_SQL, onPrepare = {
            it.setString(1, playerId)
            it.setString(2, ItemLocation.PAPERDOLL)
        }) {
            val itemId = it.getInt(3)
            val template = itemsTemplates[itemId] ?: throw IllegalStateException("Unable to find template for item id $itemId")
            EquipmentObject(it.getInt(2), template).apply {
                equippedSlot = PaperDollSlot.fromSlotData(it.getInt(6))
            }
        }
    }

    fun getAllObjectIds(): List<Int> {
        return queryList(ItemsSQL.SELECT_ITEMS_IDS_SQL) {
            it.getInt(1)
        }
    }
}