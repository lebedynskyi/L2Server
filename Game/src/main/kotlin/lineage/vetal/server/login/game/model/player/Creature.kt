package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.game.GameObject
import lineage.vetal.server.login.game.model.player.status.CreatureStatus
import java.util.*

abstract class Creature(
    id: UUID,
    name: String,
    var position: SpawnLocation
) : GameObject(id, name) {
    abstract val status: CreatureStatus
}