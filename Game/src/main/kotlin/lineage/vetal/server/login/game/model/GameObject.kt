package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.position.SpawnPosition

open class GameObject(
    var objectId: Int = 0,
    var name: String,
    var position: SpawnPosition
) {
    lateinit var region: WorldRegion
}