package lineage.vetal.server.login.game.model.inventory

// Base class for Inventory. Any inventory. private WH, clan WH, Pet, Char
abstract class ItemContainer {
    open val inventoryLimit : Int get() = 80
}