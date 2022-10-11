package lineage.vetal.server.login

import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import lineage.vetal.server.core.NetworkConfig
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.bridgeclient.BridgeGameClient
import lineage.vetal.server.login.bridgeclient.BridgeGameClientFactory
import lineage.vetal.server.login.bridgeclient.BridgeGamePacketHandler
import lineage.vetal.server.login.bridgeclient.packets.server.BridgeConnected
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.GameClientFactory
import lineage.vetal.server.login.gameclient.GamePacketHandler
import vetal.server.network.SelectorThread

class GameServer(
    private val context: GameContext
) {
    val TAG = "GameServer"

    private val bridgeSelector: SelectorThread<BridgeGameClient>
    private val bridgeCoroutineContext = newSingleThreadContext("BridgeClient")
    private val bridgePacketHandler: BridgeGamePacketHandler

    private val gameSelector: SelectorThread<GameClient>
    private val gameCoroutineContext = newSingleThreadContext("GameServer")
    private val gamePacketHandler = GamePacketHandler(context)

    init {
        writeSection(TAG)
        bridgePacketHandler = BridgeGamePacketHandler(context)
        bridgeSelector = SelectorThread(
            context.config.bridgeServer.hostname,
            context.config.bridgeServer.port,
            BridgeGameClientFactory(),
            isServer = false
        )

        val networkConfig = NetworkConfig(context.config.serverInfo.ip, context.config.serverInfo.port)
        gameSelector = SelectorThread(networkConfig.hostname, networkConfig.port, GameClientFactory())
    }

    suspend fun connectToBridge() {
        bridgeSelector.start()

        withContext(bridgeCoroutineContext) {
            launch {
                bridgeSelector.connectionAcceptFlow.collect {
                    bridgePacketHandler.handle(it, BridgeConnected())
                }
            }

            launch {
                bridgeSelector.connectionReadFlow.collect {
                    bridgePacketHandler.handle(it.first, it.second)
                }
            }
        }
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