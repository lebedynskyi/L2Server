package lineage.vetal.server.login

import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import lineage.vetal.server.core.ConfigNetwork
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.GameClientFactory
import lineage.vetal.server.login.gameclient.GamePacketHandler
import vetal.server.network.SelectorThread

class GameServer(
    context: GameContext
) {
    val TAG = "GameServer"

    private val gameSelector: SelectorThread<GameClient>
    private val gameCoroutineContext = newSingleThreadContext("GameServer")
    private val gamePacketHandler = GamePacketHandler(context)

    init {
        writeSection(TAG)
        val configNetwork = ConfigNetwork(context.config.serverInfo.ip, context.config.serverInfo.port)
        gameSelector = SelectorThread(configNetwork.hostname, configNetwork.port, GameClientFactory(), TAG = "GameServerSelector")
    }

    suspend fun startClientServer() {
        gameSelector.start()

        withContext(gameCoroutineContext) {
            launch {
                gameSelector.connectionReadFlow.collect {
                    gamePacketHandler.handle(it.first, it.second)
                }
            }
        }
    }
}