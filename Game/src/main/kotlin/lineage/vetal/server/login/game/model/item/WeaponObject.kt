package lineage.vetal.server.login.game.model.item

import lineage.vetal.server.login.game.model.template.items.ItemTemplate

class WeaponObject(
    objectId: Int,
    itemTemplate: ItemTemplate
) : EquipmentObject(objectId, itemTemplate) {
    val isAugmented: Boolean = false
    val augmentationId: Int = 0
}