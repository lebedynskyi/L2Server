package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.template.items.ItemTemplate

class EtcItemObject(
    objectId: Int,
    ownerId: String,
    itemTemplate: ItemTemplate
) : ItemObject(objectId, ownerId, itemTemplate)