package lineage.vetal.server.login.bridgeserver

import kotlinx.coroutines.*
import lineage.vetal.server.core.NetworkConfig
import lineage.vetal.server.core.client.BridgeClient
import lineage.vetal.server.core.server.SelectorServerThread
import lineage.vetal.server.login.LoginLobby


class BridgeServer(
    private val loginLobby: LoginLobby,
    private val bridgeNetworkConfig: NetworkConfig
) {
    private var selectorThread: SelectorServerThread<BridgeClient>? = null
    private val serverContext = newSingleThreadContext("Bridge")

    suspend fun startServer() {
        selectorThread = SelectorServerThread(bridgeNetworkConfig, BridgeFactory()).apply {
            start()
        }

        withContext(serverContext) {
            launch {
                selectorThread?.connectionReadFlow?.collect {
                    loginLobby.onBridgePacketReceived(it.first, it.second)
                }
            }

            launch {
                selectorThread?.connectionCloseFlow?.collect {
                    loginLobby.onBridgeClientDisconnected(it)
                }
            }
        }
    }
}