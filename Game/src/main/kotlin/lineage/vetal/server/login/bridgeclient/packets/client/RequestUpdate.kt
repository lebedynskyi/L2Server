package lineage.vetal.server.login.bridgeclient.packets.client

import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.core.server.toByte

class RequestUpdate(
    private val serverStatus: ServerStatus
) : SendablePacket() {

    override fun write() {
        writeC(0x03)

        writeD(serverStatus.id)
        writeD(serverStatus.onlineCount)
        writeC(serverStatus.isOnline.toByte())
    }
}