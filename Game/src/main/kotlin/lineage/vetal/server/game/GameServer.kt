package lineage.vetal.server.game

import kotlinx.coroutines.*
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.GameClientFactory
import lineage.vetal.server.game.gameserver.packet.GamePacket
import vetal.server.sock.SockSelector

class GameServer(
    private val context: GameContext
) {
    private val gameSelector: SockSelector<GameClient>
    private val gameCoroutineScope = CoroutineScope(newSingleThreadContext("GameServer") + Job())

    init {
        gameSelector = SockSelector(
            context.gameConfig.serverInfo.ip,
            context.gameConfig.serverInfo.port,
            GameClientFactory(),
            isServer = true,
            TAG = "GameServerSelector"
        )
    }

    fun startServer() {
        gameSelector.start()

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