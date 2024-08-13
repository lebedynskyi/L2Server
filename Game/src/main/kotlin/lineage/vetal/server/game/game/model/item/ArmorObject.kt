package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.template.items.ArmorItemTemplate

class ArmorObject(
    objectId: Int,
    override val template: ArmorItemTemplate,
) : EquipmentObject(objectId, template)