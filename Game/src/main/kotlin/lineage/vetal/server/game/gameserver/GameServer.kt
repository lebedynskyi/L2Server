package lineage.vetal.server.game.gameserver

import kotlinx.coroutines.*
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.PacketHandler
import lineage.vetal.server.game.gameserver.packet.GamePacket
import vetalll.server.sock.SelectorThread

private const val TAG = "GameServer"

class GameServer(
    private val context: GameContext,
    private val packetHandler: PacketHandler
) {
    private val gameCoroutineScope = CoroutineScope(newSingleThreadContext("GameServer") + Job())
    private val gameSelector: SelectorThread<GameClient> = SelectorThread(
        context.gameConfig.serverInfo.ip,
        context.gameConfig.serverInfo.port,
        GameClientFactory(),
        isServer = true,
        TAG = "GameServerSelector"
    )

    fun startServer() {
        gameSelector.startSelector()

        gameCoroutineScope.launch {
            gameSelector.connectionAcceptFlow.collect {
                // TODO Introduce some game lobby. protection. Timer
            }
        }

        gameCoroutineScope.launch {
            gameSelector.connectionReadFlow.collect {
                val client = it.first
                val packet = it.second as GamePacket?
                packetHandler.handlePacket(client, packet)
            }
        }

        gameCoroutineScope.launch {
            gameSelector.connectionCloseFlow.collect { client ->
                client.player?.let {
                    context.gameWorld.onPlayerQuit(client, it)
                }
            }
        }
    }
}