package lineage.vetal.server.login

import kotlinx.coroutines.*
import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.bridgeclient.BridgeGameClientFactory
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.bridgeclient.packets.server.BridgeConnected
import lineage.vetal.server.login.game.GameContext
import vetal.server.network.SelectorThread

class BridgeClient(
    private val context: GameContext
) {
    private val bridgeSelector: SelectorThread<BridgeClient>
    private val coroutineScope = CoroutineScope(newSingleThreadContext("GameBridgeClient") + Job())

    init {
        bridgeSelector = SelectorThread(
            context.gameConfig.bridgeServer.hostname,
            context.gameConfig.bridgeServer.port,
            BridgeGameClientFactory(),
            isServer = false,
            TAG = "BridgeClientSelector"
        )
    }

    fun startClient() {
        bridgeSelector.start()

        coroutineScope.launch {
            bridgeSelector.connectionAcceptFlow.collect {
                val packet = BridgeConnected()
                packet.execute(it, context)
            }
        }

        coroutineScope.launch {
            bridgeSelector.connectionReadFlow.collect {
                val packet = it.second as BridgeGamePacket?
                packet?.execute(it.first, context)
            }
        }
    }
}