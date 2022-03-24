package lineage.vetal.server.login

import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import lineage.vetal.server.core.server.SelectorClientThread
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.bridgeclient.BridgeGameClient
import lineage.vetal.server.login.bridgeclient.BridgeGameClientFactory
import lineage.vetal.server.login.bridgeclient.BridgeGamePacketHandler
import lineage.vetal.server.login.bridgeclient.packets.server.BridgeConnected

class GameServer(
    private val context: GameContext
) {
    val TAG = "GameServer"

    private val bridgeSelector: SelectorClientThread<BridgeGameClient>
    private val bridgeCoroutineContext = newSingleThreadContext("BridgeClient")
    private val bridgePacketHandler : BridgeGamePacketHandler

    init {
        writeSection(TAG)
        bridgePacketHandler = BridgeGamePacketHandler(context)
        bridgeSelector = SelectorClientThread(context.config.bridgeServer, BridgeGameClientFactory())
    }

    suspend fun startServer() {
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
}