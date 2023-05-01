package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.GameObject
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.template.items.ItemTemplate

enum class ItemLocation {
    NONE, INVENTORY, PAPERDOLL, WAREHOUSE, CLANWH, PET, PET_EQUIP, LEASE, FREIGHT
}

abstract class ItemObject(
    objectId: Int,
    var ownerId: String?,
    val template: ItemTemplate,
    spawnPosition: SpawnPosition = SpawnPosition.zero
) : GameObject(objectId, template.name, spawnPosition), Comparable<ItemObject> {
    var count = 0

    var customType1 = template.type1
    var customType2 = template.type2
    var enchantLevel: Int = 0
    var durationLeft: Int = 0
    var itemLocation: ItemLocation = ItemLocation.NONE
    var itemLocationData: Int = 0
    var createTime: Long = System.currentTimeMillis()

    override fun compareTo(other: ItemObject): Int {
        val comparison = other.createTime.compareTo(createTime)
        if (comparison != 0) {
            return comparison
        }

        return other.objectId.compareTo(objectId)
    }
}