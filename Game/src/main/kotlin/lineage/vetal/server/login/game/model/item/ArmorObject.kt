package lineage.vetal.server.login.game.model.item

import lineage.vetal.server.login.game.model.template.items.ItemTemplate

class ArmorObject(
    objectId: Int,
    ownerId: String,
    itemTemplate: ItemTemplate,
) : EquipmentObject(objectId, ownerId, itemTemplate)