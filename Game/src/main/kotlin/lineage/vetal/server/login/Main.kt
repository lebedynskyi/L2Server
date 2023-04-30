package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.game.GameContext

private const val TAG = "MainKt"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("application requires data folder as first argument")
    }

    val dataFolder = args[0]
    val gameContext = GameContext(dataFolder,)

    GameServer(gameContext).startServer()
    BridgeClient(gameContext).startClient()

    writeInfo(TAG, "Finished Game initialization")
}