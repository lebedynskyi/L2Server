package lineage.vetal.server.core.server

import lineage.vetal.server.core.model.ConfigIpAddress

abstract class BaseServer(
    val ipAddress: ConfigIpAddress
) {
    val TAG = javaClass.simpleName

    abstract suspend fun startServer()
}