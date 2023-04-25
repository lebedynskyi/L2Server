package lineage.vetal.server.login.game.model.item

import lineage.vetal.server.login.game.model.GameObject
import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.template.items.ItemTemplate

abstract class ItemObject(
    objectId: Int,
    val itemTemplate: ItemTemplate,
    spawnPosition: SpawnPosition = SpawnPosition.zero
) : GameObject(objectId, itemTemplate.name, spawnPosition) {
    var count = 0
}