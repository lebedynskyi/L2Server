package lineage.vetal.server.login.game

import lineage.vetal.server.login.game.model.location.SpawnLocation
import java.util.UUID

open class GameObject(
    val id: UUID,
    var name: String,
    var position: SpawnLocation
) {
    var objectId: Int = 0
    var isVisible = false
    var region: WorldRegion? = null
}