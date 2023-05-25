package lineage.vetal.server.login.bridgeserver

import kotlinx.coroutines.*
import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.bridgeserver.packets.BridgeClientPacket
import lineage.vetal.server.login.bridgeserver.packets.client.ClientDisconnected
import vetal.server.sock.SockSelector

class BridgeServer(
    private val context: LoginContext
) {
    private var selectorThread: SockSelector<BridgeClient>
    private val serverScope = CoroutineScope(Dispatchers.IO + Job())

    init {
        selectorThread = SockSelector(
            context.loginConfig.bridgeServer.hostname,
            context.loginConfig.bridgeServer.port,
            BridgeFactory(),
            isServer = true,
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