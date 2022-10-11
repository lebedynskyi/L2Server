package lineage.vetal.server.login.bridgeserver

import kotlinx.coroutines.*
import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.LoginContext
import vetal.server.network.SelectorThread


class BridgeServer(
    private val context: LoginContext
) {
    private var selectorThread: SelectorThread<BridgeClient>? = null
    private val serverContext = newSingleThreadContext("Bridge")
    private val bridgePacketHandler = BridgePacketHandler(context)

    suspend fun startServer() {
        selectorThread = SelectorThread(
            context.config.bridgeServer.hostname,
            context.config.bridgeServer.port,
            BridgeFactory(),
            isServer = true
        ).apply {
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