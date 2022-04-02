package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.GameObject
import lineage.vetal.server.login.game.model.player.status.CreatureStatus
import java.util.*

open class Creature(
    id: UUID,
    name: String,
): GameObject(id, name) {
    open val status = CreatureStatus()
}