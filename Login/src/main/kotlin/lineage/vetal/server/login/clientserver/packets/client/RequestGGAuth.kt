package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.LoginState
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket
import lineage.vetal.server.login.clientserver.packets.server.GGAuth
import lineage.vetal.server.login.clientserver.packets.server.LoginFail

class RequestGGAuth : LoginClientPacket() {
    var sessionId: Int = -1
    var _data1: Int = -1
    var _data2: Int = -1
    var _data3: Int = -1
    var _data4: Int = -1

    override fun execute(client: LoginClient, context: LoginContext) {
        if (client.loginState == LoginState.CONNECTED && sessionId == client.sessionId) {
            client.loginState = LoginState.AUTH_GG
            client.sendPacket(GGAuth(client.sessionId))
        } else {
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
            context.loginLobby.removeClient(client)
        }
    }

    override fun read() {
        sessionId = readD()
        _data1 = readD()
        _data2 = readD()
        _data3 = readD()
        _data4 = readD()
    }
}