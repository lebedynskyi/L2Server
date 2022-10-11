package lineage.vetal.server.login.clientserver.packets

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import vetal.server.network.ReceivablePacket

abstract class LoginClientPacket : ReceivablePacket() {
    abstract fun execute(client: LoginClient, context: LoginContext)
}