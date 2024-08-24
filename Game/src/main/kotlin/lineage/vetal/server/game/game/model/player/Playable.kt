package lineage.vetal.server.game.game.model.player

import lineage.vetal.server.game.game.model.behaviour.CreatureBehaviour
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.template.pc.CreatureTemplate

// Base class for all objects that can be controlled by player. Summon /  servitor.
abstract class Playable(
    objectId: Int,
    override var position: SpawnPosition,
    override val template: CreatureTemplate,
    override val behaviour: CreatureBehaviour
) : CreatureObject(objectId, behaviour, template, position) {
    var clientPosition: SpawnPosition? = null
}