package lineage.vetal.server.login.bridgeclient

import lineage.vetal.server.core.client.BridgeConnection
import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.model.ServerStatus

class BridgeGameClient(
    override val connection: BridgeConnection
) : Client() {

    lateinit var status: ServerStatus
}