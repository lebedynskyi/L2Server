package lineage.vetal.server.core.bridge

import lineage.vetal.server.core.model.RegisteredServer
import vetalll.server.sock.SockClient

class BridgeClient(
    override val connection: BridgeConnection
) : SockClient(connection) {
    var serverInfo: RegisteredServer? = null
}