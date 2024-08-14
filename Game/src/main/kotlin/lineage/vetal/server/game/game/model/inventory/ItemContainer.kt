package lineage.vetal.server.game.game.model.inventory

import lineage.vetal.server.game.game.model.item.ItemObject
import java.util.concurrent.ConcurrentSkipListSet

// Base class for Inventory. Any inventory. private WH, clan WH, Pet, Char
abstract class ItemContainer {
    val items: ConcurrentSkipListSet<ItemObject> = ConcurrentSkipListSet()

    open val inventoryLimit: Int get() = 80

    fun addItem(item: ItemObject) {
        items.add(item)
    }

    fun addItems(items: Iterable<ItemObject>) {
        this.items.addAll(items)
    }

    fun getItem(objectId: Int): ItemObject? {
        return items.firstOrNull { it.objectId == objectId }
    }

    fun removeItem(item: ItemObject) {
        items.remove(item)
    }
}