package lineage.vetal.server.login

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val TAG = "Game"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("application requires data folder as first argument")
    }

    val dataFolder = args[0]
    val gameContext = GameContext(dataFolder)
    val gameServer = GameServer(gameContext)
    val bridgeClient = BridgeClient(gameContext)

    runBlocking {
        launch {
            bridgeClient.connectToBridge()
        }

        launch {
            gameServer.startClientServer()
        }
    }
}