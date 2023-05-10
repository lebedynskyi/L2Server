package lineage.vetal.server.game.game.model.inventory

import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.player.PaperDollSlot

class WearableInventory : CreatureInventory() {
    private val wearedItems: Map<PaperDollSlot, EquipmentObject> get() = _wearedItems
    private val _wearedItems = mutableMapOf<PaperDollSlot, EquipmentObject>()

    fun equipItemFor(itemObject: EquipmentObject){

    }

    fun getItemIdFrom(paperDoll: PaperDollSlot): Int {
        return 0
    }

    fun getItemObjectIdFrom(paperDoll: PaperDollSlot): Int {
        return 0
    }

    fun getAugmentationIdFrom(paperDoll: PaperDollSlot): Int {
        return 0
    }
}