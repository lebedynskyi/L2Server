package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.client.Client

class BridgeClient(
    override val connection: BridgeConnection
) : Client() {

}