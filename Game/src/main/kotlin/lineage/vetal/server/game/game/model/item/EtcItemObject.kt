package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.behaviour.ItemBehaviour
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.template.items.EtcItemTemplate

class EtcItemObject(
    objectId: Int,
    override val template: EtcItemTemplate,
    override var position: SpawnPosition = SpawnPosition.zero,
    override val behaviour: ItemBehaviour = ItemBehaviour()
) : ItemObject(objectId, template, behaviour, position)