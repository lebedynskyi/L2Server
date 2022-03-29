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

    override fun execute(client: LoginClient, context: LoginContext) {
        val key = client.sessionKey
        if (key != null) {
            client.sendPacket(PlayOk(key.playOkID1, key.playOkID2))
        } else {
            // TODO don't remove from login lobby
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
        }
    }

    override fun read() {
        sessionKey1 = readD()
        sessionKey2 = readD()
        serverId = readH()
    }
}