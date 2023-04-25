package lineage.vetal.server.login.game.model.template.items

class ItemSlot {
    companion object {
        val Slots: MutableMap<String, Int> = mutableMapOf<String, Int>().apply {
            put("chest",  SLOT_CHEST)
            put("fullarmor",  SLOT_FULL_ARMOR)
            put("alldress",  SLOT_ALLDRESS)
            put("head",  SLOT_HEAD)
            put("hair",  SLOT_HAIR)
            put("face",  SLOT_FACE)
            put("hairall",  SLOT_HAIRALL)
            put("underwear",  SLOT_UNDERWEAR)
            put("back",  SLOT_BACK)
            put("neck",  SLOT_NECK)
            put("legs",  SLOT_LEGS)
            put("feet",  SLOT_FEET)
            put("gloves",  SLOT_GLOVES)
            put("chest,legs", SLOT_CHEST or SLOT_LEGS);
            put("rhand",  SLOT_R_HAND)
            put("lhand",  SLOT_L_HAND)
            put("lrhand",  SLOT_LR_HAND)
            put("rear;lear", SLOT_R_EAR or SLOT_L_EAR);
            put("rfinger;lfinger", SLOT_R_FINGER or SLOT_L_FINGER)
            put("none",  SLOT_NONE)
            put("wolf",  SLOT_WOLF) // for wolf
            put("hatchling",  SLOT_HATCHLING) // for hatchling
            put("strider",  SLOT_STRIDER) // for strider
            put("babypet", SLOT_BABYPET); // for babypet
        }

        const val SLOT_NONE = 0x0000
        const val SLOT_UNDERWEAR = 0x0001
        const val SLOT_R_EAR = 0x0002
        const val SLOT_L_EAR = 0x0004
        const val SLOT_LR_EAR = 0x00006
        const val SLOT_NECK = 0x0008
        const val SLOT_R_FINGER = 0x0010
        const val SLOT_L_FINGER = 0x0020
        const val SLOT_LR_FINGER = 0x0030
        const val SLOT_HEAD = 0x0040
        const val SLOT_R_HAND = 0x0080
        const val SLOT_L_HAND = 0x0100
        const val SLOT_GLOVES = 0x0200
        const val SLOT_CHEST = 0x0400
        const val SLOT_LEGS = 0x0800
        const val SLOT_FEET = 0x1000
        const val SLOT_BACK = 0x2000
        const val SLOT_LR_HAND = 0x4000
        const val SLOT_FULL_ARMOR = 0x8000
        const val SLOT_FACE = 0x010000
        const val SLOT_ALLDRESS = 0x020000
        const val SLOT_HAIR = 0x040000
        const val SLOT_HAIRALL = 0x080000

        const val SLOT_WOLF = -100
        const val SLOT_HATCHLING = -101
        const val SLOT_STRIDER = -102
        const val SLOT_BABYPET = -103

        const val SLOT_ALL_WEAPON = SLOT_LR_HAND or SLOT_R_HAND
    }
}