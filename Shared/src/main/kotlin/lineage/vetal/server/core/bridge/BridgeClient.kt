package lineage.vetal.server.core.bridge

import lineage.vetal.server.core.ServerInfo
import vetal.server.network.Client


class BridgeClient(
    override val connection: BridgeConnection
) : Client() {
    lateinit var connectedServerInfo: ServerInfo
}