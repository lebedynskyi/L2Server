package lineage.vetal.server.core.bridge

import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.model.RegisteredServer
import vetal.server.network.Client

class BridgeClient(
    override val connection: BridgeConnection
) : Client() {
    var serverInfo: RegisteredServer? = null
    var serverStatus: ServerStatus? = null
}