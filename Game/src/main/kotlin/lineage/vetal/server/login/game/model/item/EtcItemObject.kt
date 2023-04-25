package lineage.vetal.server.login.game.model.item

import lineage.vetal.server.login.game.model.template.items.ItemTemplate

class EtcItemObject(
    objectId: Int,
    ownerId: String,
    itemTemplate: ItemTemplate
) : ItemObject(objectId, ownerId, itemTemplate)