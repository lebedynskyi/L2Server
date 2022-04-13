package lineage.vetal.server.login.game.model.items

import lineage.vetal.server.login.game.model.player.Paperdoll

class Inventory(

) : ItemContainer {
    var currentWeight: Int = 0
    var weightLimit: Int = 0

    fun getInventoryLimit(): Int {
        return 80
    }

    fun getItemObjectIdFrom(paperdoll: Paperdoll): Int {
        return 0
    }

    fun getItemIdFrom(paperdoll: Paperdoll): Int {
        return 0
    }

    fun getAugmentationIdFrom(paperdoll: Paperdoll): Int {
        return 0
    }
}