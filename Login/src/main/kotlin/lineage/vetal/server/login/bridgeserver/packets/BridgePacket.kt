package lineage.vetal.server.login.bridgeserver.packets

import lineage.vetal.server.login.bridgeserver.BridgeClient
import lineage.vetal.server.login.LoginContext
import vetal.server.network.ReceivablePacket

abstract class BridgePacket : ReceivablePacket() {
    abstract fun execute(client: BridgeClient, context: LoginContext)
}