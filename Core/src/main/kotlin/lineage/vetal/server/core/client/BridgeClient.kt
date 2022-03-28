package lineage.vetal.server.core.client

import lineage.vetal.server.core.ServerInfo


class BridgeClient(
    override val connection: BridgeConnection
) : Client() {
    lateinit var connectedServerInfo: ServerInfo
}