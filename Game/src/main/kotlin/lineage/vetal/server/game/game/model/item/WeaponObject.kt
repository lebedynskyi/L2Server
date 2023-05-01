package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.template.items.ItemTemplate

class WeaponObject(
    objectId: Int,
    ownerId: String,
    itemTemplate: ItemTemplate
) : EquipmentObject(objectId, ownerId, itemTemplate) {
    val isAugmented: Boolean = false
    val augmentationId: Int = 0
}