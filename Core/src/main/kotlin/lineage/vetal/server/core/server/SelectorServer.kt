package lineage.vetal.server.core.server

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import lineage.vetal.server.core.model.ConfigIpAddress


class SelectorServer(
    private val configIpAddress: ConfigIpAddress
) {
    val TAG = "SocketServer"

    fun startServer(): Flow<SocketConnection> {
        return channelFlow {
            for (i in 0..15) {
                trySend(SocketConnection(i))
                delay(1000)
            }
            close()
        }
    }
}