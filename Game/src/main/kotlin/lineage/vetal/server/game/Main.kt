package lineage.vetal.server.game

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.GameContext

private const val TAG = "LoginMain"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("application requires data folder as first argument")
    }

    writeInfo(TAG, "Create and load context")
    val gameContext = GameContext()
    val dataFolder = args[0]
    gameContext.load(dataFolder)

    writeInfo(TAG, "Start Game client server")
    GameServer(gameContext).startServer()

    writeInfo(TAG, "Start Bridge client")
    BridgeClient(gameContext).startClient()

    writeInfo(TAG, "Finished Game initialization")
}