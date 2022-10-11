package lineage.vetal.server.login.clientserver.packets.server

import lineage.vetal.server.core.utils.ext.ifNullOrBlank
import lineage.vetal.server.core.utils.ext.toByte
import lineage.vetal.server.login.model.ServerInfo
import vetal.server.network.SendablePacket
import java.net.InetAddress

class ServerList(
    private val servers: List<ServerInfo>
) : SendablePacket() {
    override fun write() {
        writeC(0x04)
        writeC(servers.size)
        // Last server
        writeC(2)

        servers.forEach {
            writeC(it.config.id)
            // IP
            val raw = InetAddress.getByName(it.status?.ip?.ifNullOrBlank { it.config.ip }).address
            writeC(0xff and raw[0].toInt())
            writeC(0xff and raw[1].toInt())
            writeC(0xff and raw[2].toInt())
            writeC(0xff and raw[3].toInt())

            writeD(it.config.port)
            writeC(it.config.ageLimit)
            writeC(it.config.isPvp.toByte())
            writeH(it.status?.onlineCount ?: 0)
            writeH(it.config.maxOnline)
            writeC(it.status?.isOnline.toByte())

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