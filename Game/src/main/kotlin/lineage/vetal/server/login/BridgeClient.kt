package lineage.vetal.server.login

import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.bridgeclient.BridgeGameClientFactory
import lineage.vetal.server.login.bridgeclient.BridgeGamePacketHandler
import lineage.vetal.server.login.bridgeclient.packets.server.BridgeConnected
import vetal.server.network.SelectorThread

class BridgeClient(
    context: GameContext
) {
    private val bridgeSelector: SelectorThread<BridgeClient>
    private val bridgeCoroutineContext = newSingleThreadContext("BridgeClient")
    private val bridgePacketHandler: BridgeGamePacketHandler

    init {
        bridgePacketHandler = BridgeGamePacketHandler(context)
        bridgeSelector = SelectorThread(
            context.config.bridgeServer.hostname,
            context.config.bridgeServer.port,
            BridgeGameClientFactory(),
            isServer = false,
            TAG = "BridgeClientSelector"
        )
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
}