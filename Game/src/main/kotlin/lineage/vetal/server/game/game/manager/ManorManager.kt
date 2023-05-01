package lineage.vetal.server.game.game.manager

class ManorManager {
    val manorList: Array<String> get() = MANORS

    companion object {
        private val MANORS = arrayOf(
            "gludio",
            "dion",
            "giran",
            "oren",
            "aden",
            "innadril",
            "goddard",
            "rune",
            "schuttgart"
        )
    }
}