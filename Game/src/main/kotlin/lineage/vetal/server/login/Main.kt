package lineage.vetal.server.login

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.core.utils.streams.openResource
import lineage.vetal.server.login.game.GameWorld

const val TAG = "Game"

private const val PATH_SERVER_CONFIG = "config/Server.yaml"
private const val PATH_GAME_CONFIG = "config/Game.yaml"

fun main() {
    writeSection(TAG)
    writeInfo(TAG, "Reading configs from $PATH_SERVER_CONFIG")

    val configInputStream = openResource(PATH_SERVER_CONFIG)
    val config = GameConfig.read(configInputStream)
    val gameContext = GameContext(config)
    val gameServer = GameServer(gameContext)

    runBlocking {
        launch {
            gameServer.connectToBridge()
        }

        launch {
            gameServer.startClientServer()
        }
    }
}