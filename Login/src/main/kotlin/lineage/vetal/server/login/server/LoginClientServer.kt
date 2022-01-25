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
        val connectionFactory = ClientConnectionFactory(filter)
        val selectorServer = SocketSelectorThread(networkSettings, connectionFactory)
        selectorServer.start().collect {
            writeDebug(TAG, "On connection accepted $it")
            it.sendPacket(SendablePacket())
        }
    }
}