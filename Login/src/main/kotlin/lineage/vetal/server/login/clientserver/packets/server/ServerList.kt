package lineage.vetal.server.login.clientserver.packets.server

import lineage.vetal.server.core.model.ServerInfo
import lineage.vetal.server.core.server.SendablePacket
import java.net.InetAddress

class ServerList(
    private val connectedServers: List<ServerInfo>
) : SendablePacket() {
    override fun write() {
        writeC(0x04)
        writeC(connectedServers.size)
        // Last server
        writeC(0x00)

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
            writeC(if (it.isPvp) 0x01 else 0x00)
            writeH(it.onlineCount)
            writeH(it.maxOnline)
            writeC(if (it.isOnline) 0x01 else 0x00)

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