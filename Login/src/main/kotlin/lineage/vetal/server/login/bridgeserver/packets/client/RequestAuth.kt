package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.core.model.ServerInfo
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.server.toBoolean
import lineage.vetal.server.core.server.toByte

class RequestAuth(
) : ReceivablePacket() {
    lateinit var serverInfo: ServerInfo

    override fun read() {
        serverInfo = ServerInfo(
            readD(),
            readS(),
            readD(),
            readD(),
            readC().toBoolean(),
            readD()
        )
    }
}