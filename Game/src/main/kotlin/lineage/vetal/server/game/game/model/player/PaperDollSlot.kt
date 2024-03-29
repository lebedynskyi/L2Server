package lineage.vetal.server.game.game.model.player

enum class PaperDollSlot(val id: Int) {
    NONE(-1), UNDER(0), LEAR(1), REAR(2), NECK(3), LFINGER(4),
    RFINGER(5), HEAD(6), RHAND(7), LHAND(8), GLOVES(9), CHEST(10),
    LEGS(11), FEET(12), CLOAK(13), FACE(14), HAIR(15), HAIRALL(16);

    companion object {
        fun fromName(name: String?): PaperDollSlot {
            for (paperdoll in values()) {
                if (paperdoll.toString().equals(name, ignoreCase = true))
                    return paperdoll
            }
            return NONE
        }

        fun fromId(id: Int): PaperDollSlot {
            for (paperdoll in values()) {
                if (paperdoll.id == id) return paperdoll
            }
            return NONE
        }
    }
}