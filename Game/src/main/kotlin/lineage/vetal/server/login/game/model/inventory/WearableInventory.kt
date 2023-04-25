package lineage.vetal.server.login.game.model.inventory

import lineage.vetal.server.login.game.model.player.Paperdoll

class WearableInventory: CreatureInventory() {
    // TODO add methods to wear and unwear.. Save status of item ? Is it attribute of item?

    @Deprecated("Should be different. Why wee need to use find method? Equiped field  for char invenotry ?")
    fun getItemIdFrom(paperdoll: Paperdoll): Int {
        return 0
    }

    @Deprecated("Should be different. Why wee need to use find method? Equiped field  for char invenotry ?")
    fun getItemObjectIdFrom(paperdoll: Paperdoll): Int {
        return 0
    }

    @Deprecated("Should be different. Why wee need to use find method? Equiped field  for char invenotry ?")
    fun getAugmentationIdFrom(paperdoll: Paperdoll): Int {
        return 0
    }
}