package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket
import lineage.vetal.server.login.clientserver.packets.server.LoginFail
import lineage.vetal.server.login.clientserver.packets.server.PlayOk

class RequestServerLogin : LoginClientPacket() {
    var sessionKey1: Int = -1
    var sessionKey2: Int = -1
    var serverId = Int.MIN_VALUE

    override fun read() {
        sessionKey1 = readD()
        sessionKey2 = readD()
        serverId = readH()
    }

    override fun execute(client: LoginClient, context: LoginContext) {
       context.loginLobby.requestServerLogin(client, serverId, sessionKey1, sessionKey2)
    }
}