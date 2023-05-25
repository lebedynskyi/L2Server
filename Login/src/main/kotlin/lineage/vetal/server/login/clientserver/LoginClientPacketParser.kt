package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.encryption.CryptUtil
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.clientserver.packets.client.RequestAuthLogin
import lineage.vetal.server.login.clientserver.packets.client.RequestGGAuth
import lineage.vetal.server.login.clientserver.packets.client.RequestServerList
import lineage.vetal.server.login.clientserver.packets.client.RequestServerLogin
import vetal.server.network.PacketParser
import vetal.server.network.ReceivablePacket
import java.nio.ByteBuffer

class LoginClientPacketParser : PacketParser {

    private val TAG = "LoginClientPacketParser"

    override fun parsePacket(buffer: ByteBuffer, sBuffer: StringBuffer, size: Int): ReceivablePacket? {
        val valid = CryptUtil.verifyChecksum(buffer.array(), buffer.position(), size)
        if (!valid) {
            writeInfo(TAG, "Checksum not valid")
            return null
        }

        return when (val opCode = buffer.get().toInt()) {
            0x00 -> RequestAuthLogin()
            0x02 -> RequestServerLogin()
            0x05 -> RequestServerList()
            0x07 -> RequestGGAuth()
            else -> {
                writeDebug(TAG, "Unknown packet with opcode $opCode")
                null
            }
        }
    }
}