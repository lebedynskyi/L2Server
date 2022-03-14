package lineage.vetal.server.login.bridgeserver.packets.client

import lineage.vetal.server.core.model.ServerInfo
import lineage.vetal.server.core.server.ReceivablePacket

class RequestUpdate: ReceivablePacket() {
    private lateinit var serverInfo: ServerInfo

    override fun read() {

    }
}