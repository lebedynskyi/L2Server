package lineage.vetal.server.login.game.model.inventory

open class CreatureInventory : ItemContainer() {
    var currentWeight: Int = 0
    var weightLimit: Int = 0
}