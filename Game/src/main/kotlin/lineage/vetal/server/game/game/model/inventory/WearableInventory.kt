package lineage.vetal.server.game.game.model.inventory

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.template.items.ItemSlot

private const val TAG = "WearableInventory"

class WearableInventory : CreatureInventory() {
    fun unEquip(slot: PaperDollSlot) : EquipmentObject?{
        return items.filterIsInstance<EquipmentObject>()
            .firstOrNull { it.equippedSlot == slot }?.apply {
                equippedSlot = null
            }
    }

    fun unEquip(item: EquipmentObject) {
        items.filterIsInstance<EquipmentObject>()
            .firstOrNull { it.objectId == item.objectId }?.let {
                it.equippedSlot = null
            }
    }

    fun equip(item: EquipmentObject) : List <EquipmentObject>{
        val unEquippedItems = mutableListOf<EquipmentObject?>()
        when (val requiredSlot = item.template.bodySlot) {
            ItemSlot.SLOT_NECK -> {
                unEquippedItems.add(unEquip(PaperDollSlot.NECK))
                item.equippedSlot = PaperDollSlot.NECK
            }

            ItemSlot.SLOT_LR_FINGER -> {
                val availableSlot =
                    if (getItemFrom(PaperDollSlot.LFINGER) == null) {
                        PaperDollSlot.LFINGER
                    } else if (getItemFrom(PaperDollSlot.RFINGER) == null) {
                        PaperDollSlot.RFINGER
                    } else null

                if (availableSlot != null) {
                    item.equippedSlot = availableSlot
                } else {
                    unEquippedItems.add(unEquip(PaperDollSlot.RFINGER))
                    item.equippedSlot = PaperDollSlot.RFINGER
                }
            }

            ItemSlot.SLOT_LR_EAR -> {
                val availableSlot =
                    if (getItemFrom(PaperDollSlot.LEAR) == null) {
                        PaperDollSlot.LEAR
                    } else if (getItemFrom(PaperDollSlot.REAR) == null) {
                        PaperDollSlot.REAR
                    } else null

                if (availableSlot != null) {
                    item.equippedSlot = availableSlot
                } else {
                    unEquippedItems.add(unEquip(PaperDollSlot.REAR))
                    item.equippedSlot = PaperDollSlot.REAR
                }
            }

            ItemSlot.SLOT_FULL_ARMOR -> {
                unEquippedItems.add(unEquip(PaperDollSlot.CHEST))
                unEquippedItems.add(unEquip(PaperDollSlot.LEGS))
                item.equippedSlot = PaperDollSlot.CHEST
            }

            ItemSlot.SLOT_GLOVES -> {
                unEquippedItems.add(unEquip(PaperDollSlot.GLOVES))
                item.equippedSlot = PaperDollSlot.GLOVES
            }

            ItemSlot.SLOT_FEET -> {
                unEquippedItems.add(unEquip(PaperDollSlot.FEET))
                item.equippedSlot = PaperDollSlot.FEET
            }

            ItemSlot.SLOT_HEAD -> {
                unEquippedItems.add(unEquip(PaperDollSlot.HEAD))
                item.equippedSlot = PaperDollSlot.HEAD
            }

            ItemSlot.SLOT_CHEST -> {
                unEquippedItems.add(unEquip(PaperDollSlot.CHEST))
                item.equippedSlot = PaperDollSlot.CHEST
            }

            ItemSlot.SLOT_LEGS -> {
                val equippedChest = getItemFrom(PaperDollSlot.CHEST)
                if (equippedChest?.template?.bodySlot == ItemSlot.SLOT_FULL_ARMOR) {
                    unEquippedItems.add(unEquip(PaperDollSlot.CHEST))
                }
                item.equippedSlot = PaperDollSlot.LEGS
            }

            else -> {
                writeError(TAG, "Unable equip item. Required slot is '$requiredSlot'")
            }
        }

        return unEquippedItems.filterNotNull()
    }

    fun getAugmentationIdFromSlot(slot: PaperDollSlot): Int {
        return 0
    }

    fun getItemObjectIdFrom(slot: PaperDollSlot): Int {
        return items.filterIsInstance<EquipmentObject>()
            .firstOrNull { it.equippedSlot == slot }?.objectId ?: 0
    }

    fun getItemFrom(slot: PaperDollSlot): EquipmentObject? {
        return items.filterIsInstance<EquipmentObject>()
            .firstOrNull { it.equippedSlot == slot }
    }

    fun getItemIdFrom(slot: PaperDollSlot): Int {
        return items.filterIsInstance<EquipmentObject>()
            .firstOrNull { it.equippedSlot == slot }?.template?.id ?: 0
    }
}