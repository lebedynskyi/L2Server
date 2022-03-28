package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.server.toBoolean

class RequestUpdate : ReceivablePacket() {
    lateinit var serverStatus: ServerStatus

    override fun read() {
        serverStatus = ServerStatus(
            readD(),
            readD(),
            readC().toBoolean(),
        )
    }
}