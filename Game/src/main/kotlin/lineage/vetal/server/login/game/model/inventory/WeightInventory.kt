package lineage.vetal.server.login.game.model.inventory

open class WeightInventory : ItemContainer() {
    var currentWeight: Int = 0
    var weightLimit: Int = 0
}