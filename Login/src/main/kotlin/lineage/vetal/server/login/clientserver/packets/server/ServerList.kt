package lineage.vetal.server.login.clientserver.packets.server

import lineage.vetal.server.core.ServerInfo
import lineage.vetal.server.core.utils.ext.toByte
import vetal.server.network.SendablePacket
import java.net.InetAddress

class ServerList(
    private val connectedServers: List<ServerInfo>
) : SendablePacket() {
    override fun write() {
        writeC(0x04)
        writeC(connectedServers.size)
        // Last server
        writeC(2)

        connectedServers.forEach {
            writeC(it.id)
            // IP
            val raw = InetAddress.getByName(it.ip).address
            writeC(0xff and raw[0].toInt())
            writeC(0xff and raw[1].toInt())
            writeC(0xff and raw[2].toInt())
            writeC(0xff and raw[3].toInt())

            writeD(it.port)
            writeC(it.ageLimit)
            writeC(it.isPvp.toByte())
            writeH(it.serverStatus?.onlineCount ?: 0)
            writeH(it.maxOnline)
            writeC(it.serverStatus?.isOnline.toByte())

            var bits = 0
            // is test?
            if (false) bits = bits or 0x04

            // is showing clock
            if (false) bits = bits or 0x02

            writeD(bits)
            // Showing breckets
            writeC(if (false) 0x01 else 0x00)
        }
    }
}