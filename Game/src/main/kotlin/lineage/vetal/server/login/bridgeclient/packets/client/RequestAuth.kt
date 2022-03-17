package lineage.vetal.server.login.bridgeclient.packets.client

import lineage.vetal.server.core.model.ServerInfo
import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.core.server.toByte

class RequestAuth(
) : SendablePacket() {
    private lateinit var serverInfo: ServerInfo
    override fun write() {
        writeC(0x02)

        writeD(serverInfo.id)
        writeS(serverInfo.ip)
        writeD(serverInfo.port)
        writeD(serverInfo.ageLimit)
        writeC(serverInfo.isPvp.toByte())
        writeD(serverInfo.maxOnline)
    }
}