package lineage.vetal.server.login.bridgeserver

import kotlinx.coroutines.*
import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.server.SelectorServerThread
import lineage.vetal.server.login.LoginContext


class BridgeServer(
    private val context: LoginContext
) {
    private var selectorThread: SelectorServerThread<BridgeClient>? = null
    private val serverContext = newSingleThreadContext("Bridge")
    private val bridgePacketHandler = BridgePacketHandler(context)

    suspend fun startServer() {
        selectorThread = SelectorServerThread(context.config.bridgeServer, BridgeFactory()).apply {
            start()
        }

        withContext(serverContext) {
            launch {
                selectorThread?.connectionReadFlow?.collect {
                    bridgePacketHandler.handle(it.first, it.second)
                }
            }

            launch {
                selectorThread?.connectionCloseFlow?.collect {
                    // Nothing to do here ?
                }
            }
        }
    }
}