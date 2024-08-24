package lineage.vetal.server.game.game.model.inventory

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.item.ItemLocation
import lineage.vetal.server.game.game.model.template.items.ItemSlot

private const val TAG = "WearableInventory"

class WearableInventory : CreatureInventory() {
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

    fun unEquip(slot: PaperDollSlot): EquipmentObject? {
        return items.filterIsInstance<EquipmentObject>()
            .firstOrNull { it.equippedSlot == slot }?.apply {
                equippedSlot = null
                itemLocation = ItemLocation.None
            }
    }

    fun unEquip(item: EquipmentObject) {
        items.filterIsInstance<EquipmentObject>()
            .firstOrNull { it.objectId == item.objectId }?.let {
                it.equippedSlot = null
                it.itemLocation = ItemLocation.None
            }
    }

    fun equip(item: EquipmentObject): List<EquipmentObject> {
        val unEquippedItems = mutableListOf<EquipmentObject?>()
        when (val requiredSlot = item.template.bodySlot) {
            ItemSlot.SLOT_NECK -> {
                unEquippedItems.add(unEquip(PaperDollSlot.NECK))
                equip(item, PaperDollSlot.NECK)
            }

            ItemSlot.SLOT_LR_FINGER -> {
                val availableSlot =
                    if (getItemFrom(PaperDollSlot.RFINGER) == null) {
                        PaperDollSlot.RFINGER
                    } else if (getItemFrom(PaperDollSlot.LFINGER) == null) {
                        PaperDollSlot.LFINGER
                    } else null

                if (availableSlot != null) {
                    equip(item, availableSlot)
                } else {
                    unEquippedItems.add(unEquip(PaperDollSlot.RFINGER))
                    equip(item, PaperDollSlot.RFINGER)
                }
            }

            ItemSlot.SLOT_LR_EAR -> {
                val availableSlot =
                    if (getItemFrom(PaperDollSlot.REAR) == null) {
                        PaperDollSlot.REAR
                    } else if (getItemFrom(PaperDollSlot.LEAR) == null) {
                        PaperDollSlot.LEAR
                    } else null

                if (availableSlot != null) {
                    equip(item, availableSlot)
                } else {
                    unEquippedItems.add(unEquip(PaperDollSlot.REAR))
                    equip(item, PaperDollSlot.REAR)
                }
            }

            ItemSlot.SLOT_FULL_ARMOR -> {
                unEquippedItems.add(unEquip(PaperDollSlot.CHEST))
                unEquippedItems.add(unEquip(PaperDollSlot.LEGS))
                equip(item, PaperDollSlot.CHEST)
            }

            ItemSlot.SLOT_GLOVES -> {
                unEquippedItems.add(unEquip(PaperDollSlot.GLOVES))
                equip(item, PaperDollSlot.GLOVES)
            }

            ItemSlot.SLOT_FEET -> {
                unEquippedItems.add(unEquip(PaperDollSlot.FEET))
                equip(item, PaperDollSlot.FEET)
            }

            ItemSlot.SLOT_HEAD -> {
                unEquippedItems.add(unEquip(PaperDollSlot.HEAD))
                equip(item, PaperDollSlot.HEAD)
            }

            ItemSlot.SLOT_CHEST -> {
                unEquippedItems.add(unEquip(PaperDollSlot.CHEST))
                equip(item, PaperDollSlot.CHEST)
            }

            ItemSlot.SLOT_LEGS -> {
                val equippedChest = getItemFrom(PaperDollSlot.CHEST)
                if (equippedChest?.template?.bodySlot == ItemSlot.SLOT_FULL_ARMOR) {
                    unEquippedItems.add(unEquip(PaperDollSlot.CHEST))
                }
                equip(item, PaperDollSlot.LEGS)
            }

            ItemSlot.SLOT_R_HAND -> {
                unEquippedItems.add(unEquip(PaperDollSlot.RHAND))
                equip(item, PaperDollSlot.RHAND)
            }

            ItemSlot.SLOT_L_HAND -> {
                val equippedWeapon = getItemFrom(PaperDollSlot.RHAND)
                if (equippedWeapon?.template?.bodySlot == ItemSlot.SLOT_LR_HAND) {
                    unEquippedItems.add(unEquip(PaperDollSlot.RHAND))
                }
                unEquippedItems.add(unEquip(PaperDollSlot.LHAND))
                equip(item, PaperDollSlot.LHAND)
            }

            ItemSlot.SLOT_LR_HAND -> {
                unEquippedItems.add(unEquip(PaperDollSlot.LHAND))
                unEquippedItems.add(unEquip(PaperDollSlot.RHAND))
                equip(item, PaperDollSlot.RHAND)
            }

            else -> {
                writeError(TAG, "Unable to equip item. Required slot is '$requiredSlot'")
            }
        }

        return unEquippedItems.filterNotNull()
    }

    private fun equip(item: EquipmentObject, slot: PaperDollSlot) {
        item.equippedSlot = slot
        item.itemLocation = ItemLocation.PaperDoll(slot)
    }
}