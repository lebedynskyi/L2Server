package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket

class RequestGGAuth : LoginClientPacket() {
    var sessionId: Int = -1
    private var _data1: Int = -1
    private var _data2: Int = -1
    private var _data3: Int = -1
    private var _data4: Int = -1

    override fun read() {
        sessionId = readD()
        _data1 = readD()
        _data2 = readD()
        _data3 = readD()
        _data4 = readD()
    }

    override fun execute(client: LoginClient, context: LoginContext) {
        context.loginLobby.requestGGAuth(client, sessionId)
    }
}