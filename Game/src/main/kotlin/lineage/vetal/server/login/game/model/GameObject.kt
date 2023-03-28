package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.position.SpawnPosition
import java.util.UUID

// TODO get rid of ID - UUID. Need to use objectId
open class GameObject(
    val id: UUID,
    var name: String,
    var position: SpawnPosition
) {
    lateinit var region: WorldRegion
    var objectId: Int = 0
    var isVisible = false
}