package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeDebug

const val TAG = "Game"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("application requires data folder as first argument")
    }

    val dataFolder = args[0]
    val gameContext = GameContext(dataFolder)

    GameServer(gameContext).startServer()
    BridgeClient(gameContext).startClient()

    writeDebug(TAG, "Finished Game initialization")
}