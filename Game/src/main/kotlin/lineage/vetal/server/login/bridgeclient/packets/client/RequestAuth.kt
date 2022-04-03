package lineage.vetal.server.login.bridgeclient.packets.client

import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.core.utils.ext.toByte

class RequestAuth(
    private val serverStatus: ServerStatus
) : SendablePacket() {

    override fun write() {
        writeC(0x02)

        writeD(serverStatus.id)
        writeD(serverStatus.onlineCount)
        writeC(serverStatus.isOnline.toByte())
    }
}