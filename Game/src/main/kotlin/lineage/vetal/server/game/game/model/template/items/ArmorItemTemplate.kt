package lineage.vetal.server.game.game.model.template.items

import lineage.vetal.server.game.game.model.template.items.ItemSlot.Companion.SLOT_BACK
import lineage.vetal.server.game.game.model.template.items.ItemSlot.Companion.SLOT_FACE
import lineage.vetal.server.game.game.model.template.items.ItemSlot.Companion.SLOT_HAIR
import lineage.vetal.server.game.game.model.template.items.ItemSlot.Companion.SLOT_HAIRALL
import lineage.vetal.server.game.game.model.template.items.ItemSlot.Companion.SLOT_L_EAR
import lineage.vetal.server.game.game.model.template.items.ItemSlot.Companion.SLOT_L_FINGER
import lineage.vetal.server.game.game.model.template.items.ItemSlot.Companion.SLOT_L_HAND
import lineage.vetal.server.game.game.model.template.items.ItemSlot.Companion.SLOT_NECK
import lineage.vetal.server.game.xml.StatSet

enum class ArmorType {
    NONE, LIGHT, HEAVY, MAGIC, PET, SHIELD;
}

class ArmorItemTemplate(
    set: StatSet
) : ItemTemplate(set) {
    override val type1: Int
    override val type2: Int
    val armorType: ArmorType

    init {
        var type = set.getEnum("armor_type", ArmorType::class.java, ArmorType.NONE)
        if (bodySlot == SLOT_NECK || bodySlot == SLOT_FACE || bodySlot == SLOT_HAIR || bodySlot == SLOT_HAIRALL || bodySlot and SLOT_L_EAR != 0 || bodySlot and SLOT_L_FINGER != 0 || bodySlot and SLOT_BACK != 0) {
            type1 = TYPE1_WEAPON_RING_EARRING_NECKLACE
            type2 = TYPE2_ACCESSORY
        } else {
            // retail define shield as NONE
            if (type == ArmorType.NONE && bodySlot == SLOT_L_HAND) {
                type = ArmorType.SHIELD
            }

            type1 = TYPE1_SHIELD_ARMOR
            type2 = TYPE2_SHIELD_ARMOR
        }

        armorType = type
    }
}