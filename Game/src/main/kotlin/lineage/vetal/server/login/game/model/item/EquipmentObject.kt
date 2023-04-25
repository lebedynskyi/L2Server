package lineage.vetal.server.login.game.model.item

import lineage.vetal.server.login.game.model.template.items.ItemTemplate

abstract class EquipmentObject(
    objectId: Int,
    itemTemplate: ItemTemplate,
) : ItemObject(objectId, itemTemplate) {
    var isEquipped : Boolean = false
}