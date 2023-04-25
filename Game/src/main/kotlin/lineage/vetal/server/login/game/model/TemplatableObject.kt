package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.template.Template

abstract class TemplatableObject<T : Template>(
    objectId: Int = 0,
    name: String,
    position: SpawnPosition
) : GameObject(objectId, name, position) {
    abstract val template: T
}