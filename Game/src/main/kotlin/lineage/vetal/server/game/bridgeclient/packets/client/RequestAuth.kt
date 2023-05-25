package lineage.vetal.server.game.bridgeclient.packets.client

import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.utils.ext.toByte
import vetal.server.sock.WriteablePacket

class RequestAuth(
    private val serverStatus: ServerStatus
) : WriteablePacket() {
    override val opCode: Byte = 0x02

    override fun write() {
        writeD(serverStatus.id)
        writeD(serverStatus.onlineCount)
        writeC(serverStatus.isOnline.toByte())
        writeS(serverStatus.ip)
    }
}