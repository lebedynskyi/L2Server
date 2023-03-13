package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket

class RequestServerList : LoginClientPacket() {
    var sessionKey1: Int = -1
    var sessionKey2: Int = -1

    override fun read() {
        sessionKey1 = readD()
        sessionKey2 = readD()
    }

    override fun execute(client: LoginClient, context: LoginContext) {
        context.loginLobby.requestServerList(client, sessionKey1, sessionKey2)
    }
}