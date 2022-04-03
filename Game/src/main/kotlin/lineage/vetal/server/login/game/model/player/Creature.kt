package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.GameObject
import lineage.vetal.server.login.game.model.player.status.CreatureStatus
import lineage.vetal.server.login.game.model.player.status.PlayerStatus
import java.util.*

abstract class Creature(
    id: UUID,
    name: String,
) : GameObject(id, name) {
    abstract val status: CreatureStatus
}