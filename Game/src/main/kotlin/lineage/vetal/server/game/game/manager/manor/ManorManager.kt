package lineage.vetal.server.game.game.manager.manor

import lineage.vetal.server.game.game.GameContext

class ManorManager(context: GameContext) {
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