package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket
import lineage.vetal.server.login.clientserver.packets.server.LoginFail
import lineage.vetal.server.login.clientserver.packets.server.ServerList

class RequestServerList : LoginClientPacket() {
    var sessionKey1: Int = -1
    var sessionKey2: Int = -1

    override fun execute(client: LoginClient, context: LoginContext) {
        if (client.sessionKey?.loginOkID1 != sessionKey1 || client.sessionKey?.loginOkID2 != sessionKey2) {
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
            context.loginLobby.removeClient(client)
            return
        }

        client.sendPacket(ServerList(context.loginLobby.serverList))
    }

    override fun read() {
        sessionKey1 = readD()
        sessionKey2 = readD()
    }
}