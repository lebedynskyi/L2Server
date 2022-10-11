package lineage.vetal.server.login

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.bridgeclient.BridgeGameClientFactory
import lineage.vetal.server.login.bridgeclient.BridgeGamePacketHandler
import lineage.vetal.server.login.bridgeclient.packets.server.BridgeConnected
import vetal.server.network.SelectorThread

class BridgeClient(
    context: GameContext
) {
    private val bridgeSelector: SelectorThread<BridgeClient>
    private val bridgePacketHandler: BridgeGamePacketHandler
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

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

    fun startClient() {
        bridgeSelector.start()

        coroutineScope.launch {
            bridgeSelector.connectionAcceptFlow.collect {
                bridgePacketHandler.handle(it, BridgeConnected())
            }
        }

        coroutineScope.launch {
            bridgeSelector.connectionReadFlow.collect {
                bridgePacketHandler.handle(it.first, it.second)
            }
        }
    }
}