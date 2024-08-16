package lineage.vetal.server.game.game.model.inventory

enum class PaperDollSlot(val slotData: Int) {
    LEAR(0), REAR(1), NECK(2), LFINGER(3), RFINGER(4), HEAD(5),
    RHAND(6), LHAND(7), GLOVES(8), CHEST(9), LEGS(10), FEET(11),
    UNDERWEAR(12), FACE(13), HAIR(14), HAIRALL(15);

    companion object {
        fun fromSlotData(slotData: Int): PaperDollSlot {
            return when (slotData) {
                0 -> LEAR
                1 -> REAR
                2 -> NECK
                3 -> LFINGER
                4 -> RFINGER
                5 -> HEAD
                6 -> RHAND
                7 -> LHAND
                8 -> GLOVES
                9 -> CHEST
                10 -> LEGS
                11 -> FEET
                12 -> UNDERWEAR
                13 -> FACE
                14 -> HAIR
                15 -> HAIRALL
                else -> throw IllegalArgumentException("Unknown slot data $slotData")
            }
        }
    }
}