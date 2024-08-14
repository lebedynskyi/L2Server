package lineage.vetal.server.game.game.model.inventory

open class CreatureInventory : ItemContainer() {
    open var currentWeight: Int = 0
    open var weightLimit: Int = 0
}