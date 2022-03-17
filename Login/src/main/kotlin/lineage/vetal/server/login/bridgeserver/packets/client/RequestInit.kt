package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.core.server.ReceivablePacket

class RequestInit : ReceivablePacket() {
    var serverId: Int = -1

    override fun read() {
        serverId = readD()
    }
}