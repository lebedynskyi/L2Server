package lineage.vetal.server.login.bridgeserver.packets

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.LoginContext
import vetal.server.network.ReceivablePacket

abstract class BridgeClientPacket : ReceivablePacket() {
    abstract fun execute(client: BridgeClient, context: LoginContext)
}