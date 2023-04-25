package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.GameObject
import lineage.vetal.server.login.game.model.player.status.CreatureStatus

abstract class CreatureObject(
    objectId: Int,
    name: String,
    position: SpawnPosition
) : GameObject(objectId, name, position) {
    private val TAG = "Creature"

    var title: String? = null
    var isRunning: Boolean = false
    var isInCombat: Boolean = false
    var isAlikeDead: Boolean = false
    var isFlying = false

    abstract val status: CreatureStatus
}