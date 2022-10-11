package lineage.vetal.server.core.bridge

import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.model.ServerInfo
import vetal.server.network.Client

class BridgeClient(
    override val connection: BridgeConnection
) : Client() {
    var serverInfo: ServerInfo? = null
    var serverStatus: ServerStatus? = null
}