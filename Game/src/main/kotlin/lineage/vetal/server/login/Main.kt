package lineage.vetal.server.login

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.core.utils.streams.openResource
import lineage.vetal.server.login.game.GameWorld
import lineage.vetal.server.login.gameserver.GameServer
import lineage.vetal.server.login.model.GameConfig

const val TAG = "Game"

private const val PATH_SERVER_CONFIG = "config/Server.yaml"
private const val PATH_GAME_CONFIG = "config/Game.yaml"

fun main() {
    writeSection(TAG)
    writeInfo(TAG, "Reading configs from $PATH_SERVER_CONFIG")

    val configInputStream = openResource(PATH_SERVER_CONFIG)
    val serverConfig = GameConfig.read(configInputStream)
    val gameWorld = GameWorld()

    runBlocking {
        launch {
            GameServer(gameWorld, serverConfig).startServer()
        }
    }
}