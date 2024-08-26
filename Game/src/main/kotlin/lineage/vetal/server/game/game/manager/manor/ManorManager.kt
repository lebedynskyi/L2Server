package lineage.vetal.server.game.game.manager.manor

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.server.ManorList

private const val TAG = "ManorManager"

class ManorManager(
    private val context: GameContext
) {
    val manorList: Array<String> get() = MANORS

    fun onPlayerRequestList(player: PlayerObject) {
        player.sendPacket(ManorList(manorList))
    }

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