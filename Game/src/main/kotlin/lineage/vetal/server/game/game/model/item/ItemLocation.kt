package lineage.vetal.server.game.game.model.item

import lineage.vetal.server.game.game.model.inventory.PaperDollSlot

sealed class ItemLocation(
    val name: String,
) {
    abstract val data: Int

    data class PaperDoll(val slot: PaperDollSlot) : ItemLocation(PAPERDOLL) {
        override val data: Int get() = slot.slotData
    }

    data object None : ItemLocation(NONE) {
        override val data: Int get() = 0
    }

    data object Inventory : ItemLocation(INVENTORY) {
        override val data: Int get() = 0
    }

    data object Warehouse : ItemLocation(WAREHOUSE) {
        override val data: Int get() = 0
    }

    data object ClanWh : ItemLocation(CLANWH) {
        override val data: Int get() = 0
    }

    data object Pet : ItemLocation(PET) {
        override val data: Int get() = 0
    }

    data object PetEquip : ItemLocation(PET_EQUIP) {
        override val data: Int get() = 0
    }

    data object Freight : ItemLocation(FREIGHT) {
        override val data: Int get() = 0
    }

    companion object {
        const val PAPERDOLL = "PAPERDOLL"
        const val NONE = "NONE"
        const val INVENTORY = "INVENTORY"
        const val WAREHOUSE = "WAREHOUSE"
        const val CLANWH = "CLANWH"
        const val PET = "PET"
        const val PET_EQUIP = "PET_EQUIP"
        const val FREIGHT = "FREIGHT"

        fun from(value: String, data: Int): ItemLocation {
            return when (value) {
                "PAPERDOLL" -> PaperDoll(PaperDollSlot.fromSlotData(data))
                "INVENTORY" -> Inventory
                "WAREHOUSE" -> Warehouse
                "CLANWH" -> ClanWh
                "PET" -> Pet
                "PET_EQUIP" -> PetEquip
                "FREIGHT" -> Freight
                else -> None
            }
        }
    }
}