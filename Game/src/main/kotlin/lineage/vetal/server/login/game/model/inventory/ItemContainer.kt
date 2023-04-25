package lineage.vetal.server.login.game.model.inventory

import lineage.vetal.server.login.game.model.item.ItemObject
import java.util.concurrent.ConcurrentSkipListSet

// Base class for Inventory. Any inventory. private WH, clan WH, Pet, Char
abstract class ItemContainer {
    private val items: ConcurrentSkipListSet<ItemObject> = ConcurrentSkipListSet()

    open val inventoryLimit : Int get() = 80

    fun addItem(item: ItemObject) {
        items.add(item)
    }
}