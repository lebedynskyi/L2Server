package lineage.vetal.server.game

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.game.bridgeclient.BridgeClient
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameServer

private const val TAG = "LoginMain"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("application requires data folder as first argument")
    }

    writeSection("Main")
    writeInfo(TAG, "Create and load context")
    val dataFolder = args[0]
    val gameContext = GameContext().apply {
        load(dataFolder)
    }

    writeSection("Servers")
    writeInfo(TAG, "Start Game client server")
    GameServer(gameContext).startServer()

    writeInfo(TAG, "Start Bridge client")
    BridgeClient(gameContext).startClient()

    writeInfo(TAG, "Finished Game initialization")
}