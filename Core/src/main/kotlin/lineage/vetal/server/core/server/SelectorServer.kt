package lineage.vetal.server.core.server

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import lineage.vetal.server.core.settings.NetworkConfig
import lineage.vetal.server.core.utils.logs.writeInfo

private const val TAG = "SelectorServer"

class SelectorServer(
    private val configIpAddress: NetworkConfig
) {
    fun startServer(): Flow<SocketConnection> {
        return channelFlow {
            writeInfo(TAG, "Listening clients on ${configIpAddress.hostname}:${configIpAddress.port}")
//            for (i in 0..15) {
//                trySend(SocketConnection(i))
//                delay(1000)
//            }
//            close()
        }
    }
}