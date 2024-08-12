package lineage.vetal.server.game

import kotlinx.coroutines.*
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.GameClientFactory
import lineage.vetal.server.game.gameserver.packet.GamePacket
import vetalll.server.sock.SelectorThread

class GameServer(
    private val context: GameContext
) {
    private val gameSelector: SelectorThread<GameClient> = SelectorThread(
        context.gameConfig.serverInfo.ip,
        context.gameConfig.serverInfo.port,
        GameClientFactory(),
        isServer = true,
        TAG = "GameServerSelector"
    )
    private val gameCoroutineScope = CoroutineScope(newSingleThreadContext("GameServer") + Job())

    fun startServer() {
        gameSelector.startSelector()

        gameCoroutineScope.launch {
            gameSelector.connectionReadFlow.collect {
                val packet = it.second as GamePacket?
                packet?.execute(it.first, context)
            }
        }

        gameCoroutineScope.launch {
            gameSelector.connectionCloseFlow.collect { client ->
                client.player?.let {
                    context.worldManager.onPlayerQuit(client, it)
                }
            }
        }
    }
}