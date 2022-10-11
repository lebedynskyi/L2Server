package lineage.vetal.server.login

import kotlinx.coroutines.*
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.GameClientFactory
import lineage.vetal.server.login.gameserver.GamePacketHandler
import vetal.server.network.SelectorThread

class GameServer(
    context: GameContext
) {
    private val gameSelector: SelectorThread<GameClient>
    private val gameCoroutineScope = CoroutineScope(newSingleThreadContext("GameServer") + Job())
    private val gamePacketHandler = GamePacketHandler(context)

    init {
        gameSelector = SelectorThread(
            context.config.serverInfo.ip,
            context.config.serverInfo.port,
            GameClientFactory(),
            TAG = "GameServerSelector"
        )
    }

    fun startServer() {
        gameSelector.start()

        gameCoroutineScope.launch {
            gameSelector.connectionReadFlow.collect {
                gamePacketHandler.handle(it.first, it.second)
            }
        }
    }
}