package lineage.vetal.server.game.game.model

import lineage.vetal.server.game.game.model.behaviour.GameObjectBehaviour
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.template.GameObjectTemplate

abstract class GameObject(
    val objectId: Int = 0,
    open var position: SpawnPosition,
    open val template: GameObjectTemplate,
    open val behaviour: GameObjectBehaviour
) {
    abstract val name: String

    // TODO only items that in ground or in world does have region
    // Only objects who can move spawn and even who can send packet should be responsible for it
    lateinit var region: WorldRegion

    override fun toString(): String {
        return "$objectId - $name"
    }
}