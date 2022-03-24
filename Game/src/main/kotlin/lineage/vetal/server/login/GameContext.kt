package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.game.GameWorld

class GameContext(
    val config: GameConfig,
) {
    private val TAG = "GameContext"
    val gameWorld: GameWorld

    init {
        writeSection(TAG)
        gameWorld = GameWorld()
    }
}