package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.bridge.BridgeConnection
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.login.model.ServerInfo
import vetal.server.network.Client

class BridgeClient(
    override val connection: BridgeConnection
) : Client() {
    var serverInfo: ServerInfo? = null
    var connectedServerStatus: ServerStatus? = null
}