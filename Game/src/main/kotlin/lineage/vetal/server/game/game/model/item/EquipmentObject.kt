package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.template.items.ItemTemplate

abstract class EquipmentObject(
    objectId: Int,
    override val template: ItemTemplate,
) : ItemObject(objectId, template) {
    val isEquipped: Boolean get() = equippedSlot != null
    var equippedSlot: Int? = null
}