package lineage.vetal.server.game.bridgeclient.packets.client

import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.utils.ext.toByte
import vetal.server.network.SendablePacket

class RequestUpdate(
    private val serverStatus: ServerStatus
) : SendablePacket() {

    override fun write() {
        writeC(0x03)

        writeD(serverStatus.id)
        writeD(serverStatus.onlineCount)
        writeC(serverStatus.isOnline.toByte())
        writeS(serverStatus.ip)
    }
}