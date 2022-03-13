package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.core.server.ReceivablePacket

class RequestServerLogin : ReceivablePacket() {
    var sessionKey1: Int = -1
    var sessionKey2: Int = -1
    var serverId = Int.MIN_VALUE

    override fun read() {
        sessionKey1 = readD()
        sessionKey2 = readD()
        serverId = readH()
    }
}