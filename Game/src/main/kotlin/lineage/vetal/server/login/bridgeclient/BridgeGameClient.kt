package lineage.vetal.server.login.bridgeclient

import lineage.vetal.server.core.bridge.BridgeConnection
import lineage.vetal.server.core.server.Client
import lineage.vetal.server.core.model.ServerStatus

class BridgeGameClient(
    override val connection: BridgeConnection
) : Client() {

    lateinit var status: ServerStatus
}