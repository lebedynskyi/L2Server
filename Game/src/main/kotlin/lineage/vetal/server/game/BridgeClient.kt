package lineage.vetal.server.game

import kotlinx.coroutines.*
import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.game.bridgeclient.BridgeGameClientFactory
import lineage.vetal.server.game.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.game.bridgeclient.packets.server.BridgeConnected
import lineage.vetal.server.game.game.GameContext
import vetalll.server.sock.SelectorThread

class BridgeClient(
    private val context: GameContext
) {
    private val coroutineScope = CoroutineScope(newSingleThreadContext("BridgeClient") + Job())
    private val bridgeSelector: SelectorThread<BridgeClient> = SelectorThread(
        context.gameConfig.bridgeServer.hostname,
        context.gameConfig.bridgeServer.port,
        BridgeGameClientFactory(),
        isServer = false,
        TAG = "BridgeClientSelector"
    )

    fun startClient() {
        bridgeSelector.startSelector()

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