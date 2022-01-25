package lineage.vetal.server.login.server

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.server.BaseServer
import lineage.vetal.server.core.server.SelectorServer
import lineage.vetal.server.core.settings.NetworkConfig

class LoginClientServer(
    networkSettings: NetworkConfig
) : BaseServer(networkSettings) {
    override suspend fun startServer() {
        val selector = SelectorServer(networkSettings)
        selector.startServer().collect {
            writeDebug(TAG, it)
        }
    }
}