package lineage.vetal.server.core.server

import lineage.vetal.server.core.settings.NetworkConfig

abstract class BaseServer(
    val networkSettings: NetworkConfig
) {
    protected val TAG = javaClass.simpleName

    abstract suspend fun startServer()
}