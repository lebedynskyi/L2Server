package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.encryption.CryptUtil
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.clientserver.packets.client.RequestAuthLogin
import lineage.vetal.server.login.clientserver.packets.client.RequestGGAuth
import lineage.vetal.server.login.clientserver.packets.client.RequestServerList
import lineage.vetal.server.login.clientserver.packets.client.RequestServerLogin
import vetal.server.sock.ReadablePacket
import vetal.server.sock.SockPacketFactory
import java.nio.ByteBuffer

class LoginClientPacketParser : SockPacketFactory {
    private val TAG = "LoginClientPacketParser"

    override fun parsePacket(opCode: Byte, size: Int, buffer: ByteBuffer): ReadablePacket? {
        // opcode was read before. So buffer.position() - 2 means all packet should be affected by check sum verification
        val valid = CryptUtil.verifyChecksum(buffer.array(), buffer.position() - 2, size)
        if (!valid) {
            writeInfo(TAG, "Checksum not valid")
            return null
        }

        return when (opCode.toInt()) {
            0x00 -> RequestAuthLogin()
            0x02 -> RequestServerLogin()
            0x05 -> RequestServerList()
            0x07 -> RequestGGAuth()
            else -> null
        }
    }
}