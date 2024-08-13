package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.template.items.WeaponItemTemplate

class WeaponObject(
    objectId: Int,
    override val template: WeaponItemTemplate
) : EquipmentObject(objectId, template) {

    val isAugmented: Boolean = false
    val augmentationId: Int = 0
}