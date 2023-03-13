package lineage.vetal.server.login.bridgeserver

import kotlinx.coroutines.*
import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.bridgeserver.packets.BridgeClientPacket
import lineage.vetal.server.login.bridgeserver.packets.client.ClientDisconnected
import vetal.server.network.SelectorThread

class BridgeServer(
    private val context: LoginContext
) {
    private var selectorThread: SelectorThread<BridgeClient>
    private val serverScope = CoroutineScope(Dispatchers.IO + Job())

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
                val packet = ClientDisconnected()
                packet.execute(it, context)
            }
        }

        serverScope.launch {
            selectorThread.connectionReadFlow.collect {
                val packet = it.second as BridgeClientPacket?
                packet?.execute(it.first, context)
            }
        }
    }
}