package lineage.vetal.server.login.game.model.item

import lineage.vetal.server.login.game.model.template.items.ItemTemplate

abstract class Equipment(
    objectId: Int,
    itemTemplate: ItemTemplate,
) : ItemObject(objectId, itemTemplate)