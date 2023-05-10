package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.template.items.ItemTemplate

abstract class EquipmentObject(
    objectId: Int,
    ownerId: String,
    itemTemplate: ItemTemplate,
) : ItemObject(objectId, ownerId, itemTemplate) {
    val isEquipped : Boolean get() = equippedSlot != null
    var equippedSlot: Int? = null
}