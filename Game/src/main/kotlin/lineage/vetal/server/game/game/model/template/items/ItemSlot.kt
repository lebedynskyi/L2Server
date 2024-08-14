package lineage.vetal.server.game.game.model.template.items

enum class ItemSlot(
    val gameId: Int,
    val xmlSlot: String
) {
    SLOT_CHEST(0x0400, "chest"),
    SLOT_FULL_ARMOR(0x8000, "fullarmor"),
    SLOT_ALLDRESS(0x020000, "alldress"),
    SLOT_HEAD(0x0040, "head"),
    SLOT_HAIR(0x040000, "hair"),
    SLOT_FACE(0x010000, "face"),
    SLOT_HAIRALL(0x080000, "hairall"),
    SLOT_UNDERWEAR(0x0001, "underwear"),
    SLOT_LEGS(0x0800, "legs"),
    SLOT_FEET(0x1000, "feet"),
    SLOT_GLOVES(0x0200, "gloves"),
    SLOT_R_HAND(0x0080, "rhand"),
    SLOT_L_HAND(0x0100, "lhand"),
    SLOT_LR_HAND(0x4000, "lrhand"),
    SLOT_NECK(0x0008, "neck"),
    SLOT_LR_FINGER(0x0030, "rfinger;lfinger"),
    SLOT_LR_EAR(0x0006, "rear;lear"),
    SLOT_WOLF(-100, "wolf"),
    SLOT_HATCHLING(-101, "hatchling"),
    SLOT_STRIDER(-102, "strider"),
    SLOT_BABYPET(-103, "babypet"),
    SLOT_NONE(0x0000, "none");

    companion object {
        fun fromValue(value: String?): ItemSlot {
            return entries.firstOrNull { it.xmlSlot == value } ?: ItemSlot.SLOT_NONE
        }
    }
}