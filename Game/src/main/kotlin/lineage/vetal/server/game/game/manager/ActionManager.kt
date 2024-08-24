package lineage.vetal.server.game.game.manager

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.game.model.player.PlayerObject

class ActionManager(
    private val context: GameContext
) {

    fun onPlayerAction(player: PlayerObject, target: NpcObject, originX: Int, originY: Int, originZ: Int) {
        player.target = target
    }

    fun onPlayerAction(player: PlayerObject, target: PlayerObject, originX: Int, originY: Int, originZ: Int) {
        player.target = target
    }
}