package lineage.vetal.server.login.server

import lineage.vetal.server.core.server.*
import lineage.vetal.server.core.settings.NetworkConfig
import lineage.vetal.server.core.utils.logs.writeDebug

private val TAG = "LoginClientServer"

class LoginClientServer(
    private val networkSettings: NetworkConfig
) {
    suspend fun startServer() {
        val filter = SocketConnectionFilter(emptyList())
        val connectionFactory = LoginClientFactory(filter)
        val selectorServer = SocketSelectorThread<LoginClient>(networkSettings, connectionFactory)
        selectorServer.start()
        selectorServer.connectionAcceptFlow.collect {
            writeDebug(TAG, "New client")
        }
    }
}