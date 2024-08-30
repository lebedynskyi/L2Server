package lineage.vetal.server.game.gameserver

import kotlinx.coroutines.*
import lineage.vetal.server.game.ConfigGameServer
import lineage.vetal.server.game.game.handler.PacketHandler
import lineage.vetal.server.game.gameserver.packet.GameClientPacket
import lineage.vetal.server.game.gameserver.packet.client.Connected
import lineage.vetal.server.game.gameserver.packet.client.Disconnected
import vetalll.server.sock.SelectorThread

private const val TAG = "GameServer"

class GameServer(
    gameConfig: ConfigGameServer,
    private val packetHandler: PacketHandler
) {
    private val gameCoroutineScope = CoroutineScope(newSingleThreadContext("GameServer") + Job())
    private val gameSelector: SelectorThread<GameClient> = SelectorThread(
        gameConfig.serverInfo.ip,
        gameConfig.serverInfo.port,
        GameClientFactory(),
        isServer = true,
        TAG = "GameServerSelector"
    )

    fun startServer() {
        gameSelector.startSelector()

        gameCoroutineScope.launch {
            gameSelector.connectionAcceptFlow.collect {
               packetHandler.handlePacket(it, Connected)
            }
        }

        gameCoroutineScope.launch {
            gameSelector.connectionReadFlow.collect {
                val client = it.first
                val packet = it.second as GameClientPacket
                packetHandler.handlePacket(client, packet)
            }
        }

        gameCoroutineScope.launch {
            gameSelector.connectionCloseFlow.collect { client ->
                packetHandler.handlePacket(client, Disconnected)
            }
        }
    }
}