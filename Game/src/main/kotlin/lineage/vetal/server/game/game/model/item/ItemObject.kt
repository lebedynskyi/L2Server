package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.GameObject
import lineage.vetal.server.game.game.model.behaviour.ItemBehaviour
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.template.items.ItemTemplate

abstract class ItemObject(
    objectId: Int,
    override val template: ItemTemplate,
    override val behaviour: ItemBehaviour = ItemBehaviour(),
    override var position: SpawnPosition = SpawnPosition.zero,
    override val name: String = template.name
) : GameObject(objectId, position, template, behaviour), Comparable<ItemObject> {
    var customType1: Int = 0
    var customType2: Int = 0
    var count = 0
    var ownerId: String? = null
    var enchantLevel: Int = 0
    var durationLeft: Int = 0
    var createTime: Long = System.currentTimeMillis()
    var itemLocation: ItemLocation = ItemLocation.None

    override fun compareTo(other: ItemObject): Int {
        val comparison = other.createTime.compareTo(createTime)
        if (comparison != 0) {
            return comparison
        }

        return other.objectId.compareTo(objectId)
    }

    override fun toString(): String {
        return "$objectId - ${template.name}(${template.id})"
    }
}