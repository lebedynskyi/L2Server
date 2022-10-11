package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.ConfigRegisteredServer
import lineage.vetal.server.core.bridge.BridgeConnection
import lineage.vetal.server.core.model.ServerStatus
import vetal.server.network.Client


class BridgeClient(
    override val connection: BridgeConnection
) : Client() {
    var connectedServer: ConfigRegisteredServer? = null
    var connectedServerStatus: ServerStatus? = null
}