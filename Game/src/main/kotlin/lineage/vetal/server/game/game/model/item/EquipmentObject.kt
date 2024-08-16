package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.inventory.PaperDollSlot
import lineage.vetal.server.game.game.model.template.items.ItemTemplate

open class EquipmentObject(
    objectId: Int,
    override val template: ItemTemplate,
) : ItemObject(objectId, template) {

    var equippedSlot: PaperDollSlot? = null
    val isEquipped: Boolean get() = equippedSlot != null
}