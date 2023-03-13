package lineage.vetal.server.login.bridgeserver

import kotlinx.coroutines.*
import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.LoginContext
import vetal.server.network.SelectorThread


class BridgeServer(
    private val context: LoginContext
) {
    private var selectorThread: SelectorThread<BridgeClient>
    private val serverScope = CoroutineScope(Dispatchers.IO + Job())
    private val bridgePacketHandler = BridgePacketHandler(context)

    init {
        selectorThread = SelectorThread(
            context.loginConfig.bridgeServer.hostname,
            context.loginConfig.bridgeServer.port,
            BridgeFactory(),
            TAG = "BridgeServerSelector"
        )
    }

    fun startServer() {
        selectorThread.start()

        serverScope.launch {
            selectorThread.connectionCloseFlow.collect {
                // Nothing to do here ?
            }
        }

        serverScope.launch {
            selectorThread.connectionReadFlow.collect {
                bridgePacketHandler.handle(it.first, it.second)
            }
        }
    }
}