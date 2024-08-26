package lineage.vetal.server.game.game.manager.movement

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.SpawnPosition

private const val TAG = "MovementManager"

class MovementManager(
    private val context: GameContext
) {
    fun onPlayerValidatePosition(player: PlayerObject, loc: SpawnPosition) {
        player.clientPosition = loc
    }
}