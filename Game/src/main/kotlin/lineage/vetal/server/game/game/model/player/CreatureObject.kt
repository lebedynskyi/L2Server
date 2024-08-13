package lineage.vetal.server.game.game.model.player

import lineage.vetal.server.game.game.model.GameObject
import lineage.vetal.server.game.game.model.behaviour.CreatureBehaviour
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.player.status.CreatureStats
import lineage.vetal.server.game.game.model.template.pc.CreatureTemplate

private const val TAG = "Creature"

abstract class CreatureObject(
    objectId: Int,
    override val behaviour: CreatureBehaviour,
    override val template: CreatureTemplate,
    override var position: SpawnPosition
) : GameObject(objectId, position, template, behaviour) {
    abstract val stats: CreatureStats

    var title: String? = null
    var isRunning: Boolean = false
    var isInCombat: Boolean = false
    var isAlikeDead: Boolean = false
    var isFlying = false
}
