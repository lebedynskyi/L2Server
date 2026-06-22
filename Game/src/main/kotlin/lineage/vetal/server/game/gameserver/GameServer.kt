package lineage.vetal.server.game.gameserver

import kotlinx.coroutines.*
import lineage.vetal.server.game.ConfigGameServer
import lineage.vetal.server.game.game.handler.GamePacketHandler
import lineage.vetal.server.game.gameserver.packet.GameClientPacket
import lineage.vetal.server.game.gameserver.packet.client.Connected
import lineage.vetal.server.game.gameserver.packet.client.Disconnected
import vetalll.server.sock.SelectorThread

private const val TAG = "GameServer"

class GameServer(
    gameConfig: ConfigGameServer,
    private val packetHandler: GamePacketHandler
) {
    private val gameServerScope = CoroutineScope(newSingleThreadContext("GameServer") + Job())
    private val gameSelector: SelectorThread<GameClient> = SelectorThread(
        gameConfig.serverInfo.ip,
        gameConfig.serverInfo.port,
        GameClientFactory(),
        isServer = true,
        tag = "GameServerSelector"
    )

    fun startServer() {
        gameServerScope.launch {
            gameSelector.connectionAcceptFlow.collect {
               packetHandler.handlePacket(it, Connected)
            }
        }

        gameServerScope.launch {
            gameSelector.connectionReadFlow.collect {
                val client = it.first
                val packet = it.second as GameClientPacket
                packetHandler.handlePacket(client, packet)
            }
        }

        gameServerScope.launch {
            gameSelector.connectionCloseFlow.collect { client ->
                packetHandler.handlePacket(client, Disconnected)
            }
        }

        gameSelector.startSelector()
    }
}
