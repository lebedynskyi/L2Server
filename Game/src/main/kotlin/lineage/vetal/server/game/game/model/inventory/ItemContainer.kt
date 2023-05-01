package lineage.vetal.server.game.game.model.inventory

import lineage.vetal.server.game.game.model.item.ItemObject
import java.util.concurrent.ConcurrentSkipListSet

// Base class for Inventory. Any inventory. private WH, clan WH, Pet, Char
abstract class ItemContainer {
    val items: Set<ItemObject> get() = _items
    private val _items: ConcurrentSkipListSet<ItemObject> = ConcurrentSkipListSet()

    open val inventoryLimit: Int get() = 80

    fun addItem(item: ItemObject) {
        _items.add(item)
    }

    fun addAll(items: Iterable<ItemObject>) {
        _items.addAll(items)
    }

    fun getItem(objectId: Int): ItemObject? {
        return _items.firstOrNull { it.objectId == objectId }
    }

    fun removeItem(item: ItemObject) {
        _items.remove(item)
    }
}