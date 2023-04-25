package lineage.vetal.server.login.game.model.item

import lineage.vetal.server.login.game.model.template.items.ItemTemplate

class WeaponObject(
    objectId: Int,
    itemTemplate: ItemTemplate
): Equipment(objectId, itemTemplate)