package lineage.vetal.server.login.clientserver.packets

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import vetalll.server.sock.ReadablePacket

abstract class LoginClientPacket : ReadablePacket() {
    abstract fun execute(client: LoginClient, context: LoginContext)
}