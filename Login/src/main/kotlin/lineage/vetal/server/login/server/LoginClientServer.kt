package lineage.vetal.server.login.server

import lineage.vetal.server.core.logs.writeDebug
import lineage.vetal.server.core.model.ConfigIpAddress
import lineage.vetal.server.core.server.BaseServer
import lineage.vetal.server.core.server.SelectorServer

class LoginClientServer(
    ipAddress: ConfigIpAddress
) : BaseServer(ipAddress) {
    override suspend fun startServer() {
        val selector = SelectorServer(ipAddress)
        selector.startServer().collect {
            writeDebug(TAG, it)
        }
    }
}