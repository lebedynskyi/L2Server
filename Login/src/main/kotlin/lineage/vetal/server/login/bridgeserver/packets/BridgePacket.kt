package lineage.vetal.server.login.bridgeserver.packets

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.login.LoginContext

abstract class BridgePacket : ReceivablePacket() {
    abstract fun execute(client: BridgeClient, context: LoginContext)
}