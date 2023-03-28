package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.GameObject
import lineage.vetal.server.login.game.model.player.status.CreatureStatus
import java.util.*

abstract class Creature(
    id: UUID,
    name: String,
    position: SpawnPosition
) : GameObject(id, name, position) {
    private val TAG = "Creature"

    var title: String? = null
    var isRunning: Boolean = false
    var isInCombat: Boolean = false
    var isAlikeDead: Boolean = false
    var isFlying = false

    abstract val status: CreatureStatus
}