package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.position.SpawnPosition

abstract class GameObject(
    val objectId: Int = 0,
    var name: String,
    var position: SpawnPosition
) {
    // TODO items does not have region ?
    // Only objects who can move spawn and even who can send packet should be responsible for it
    lateinit var region: WorldRegion
}