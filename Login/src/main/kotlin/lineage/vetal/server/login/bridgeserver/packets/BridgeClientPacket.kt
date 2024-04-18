package lineage.vetal.server.login.bridgeserver.packets

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.login.LoginContext
import vetalll.server.sock.ReadablePacket

abstract class BridgeClientPacket : ReadablePacket() {
    abstract fun execute(client: BridgeClient, context: LoginContext)
}