package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.core.server.ReceivablePacket

class RequestAuthLogin : ReceivablePacket() {
    val raw = ByteArray(128)
    override fun read() {
        readB(raw)
    }
}