package lineage.vetal.server.core.bridge

import lineage.vetal.server.core.ServerInfo
import lineage.vetal.server.core.server.Client


class BridgeClient(
    override val connection: BridgeConnection
) : Client() {
    lateinit var connectedServerInfo: ServerInfo
}