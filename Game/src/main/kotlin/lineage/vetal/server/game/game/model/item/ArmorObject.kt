package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.template.items.ItemTemplate

class ArmorObject(
    objectId: Int,
    ownerId: String,
    itemTemplate: ItemTemplate,
) : EquipmentObject(objectId, ownerId, itemTemplate)