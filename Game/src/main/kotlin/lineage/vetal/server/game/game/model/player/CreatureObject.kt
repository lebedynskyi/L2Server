package lineage.vetal.server.game.game.model.player

import lineage.vetal.server.game.game.model.GameObject
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.player.status.CreatureStats

private const val TAG = "Creature"

abstract class CreatureObject(
    objectId: Int,
    name: String,
    position: SpawnPosition
) : GameObject(objectId, name, position) {
    abstract val stats: CreatureStats

    var title: String? = null
    var isRunning: Boolean = false
    var isInCombat: Boolean = false
    var isAlikeDead: Boolean = false
    var isFlying = false
}
