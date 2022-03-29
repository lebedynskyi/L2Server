package lineage.vetal.server.login.clientserver.packets

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient

abstract class LoginClientPacket : ReceivablePacket() {
    abstract fun execute(client: LoginClient, context: LoginContext)
}