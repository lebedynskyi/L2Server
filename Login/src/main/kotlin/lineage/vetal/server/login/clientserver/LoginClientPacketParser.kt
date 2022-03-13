package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.encryption.CryptUtil
import lineage.vetal.server.core.server.DATA_HEADER_SIZE
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.clientserver.packets.client.RequestAuthLogin
import lineage.vetal.server.login.clientserver.packets.client.RequestGGAuth
import lineage.vetal.server.login.clientserver.packets.client.RequestServerList
import lineage.vetal.server.login.clientserver.packets.client.RequestServerLogin
import java.nio.ByteBuffer

class LoginClientPacketParser(
    private val loginCrypt: LoginClientCrypt
) {

    private val TAG = "LoginClientPacketParser"

    fun parsePacket(buffer: ByteBuffer): ReceivablePacket? {
        if (buffer.position() >= buffer.limit()) {
            return null
        }
        val header = buffer.short
        val dataSize = header - DATA_HEADER_SIZE
        val decryptedSize = loginCrypt.decrypt(buffer.array(), buffer.position(), dataSize)
        val valid = CryptUtil.verifyChecksum(buffer.array(), buffer.position(), decryptedSize)

        return if (valid) {
            parsePacketByOpCode(buffer)
        } else null
    }


    private fun parsePacketByOpCode(buffer: ByteBuffer): ReceivablePacket? {
        val packet = when (val opCode = buffer.get().toInt()) {
            0x00 -> RequestAuthLogin()
            0x02 -> RequestServerLogin()
            0x05 -> RequestServerList()
            0x07 -> RequestGGAuth()
            else -> {
                writeDebug(TAG, "Unknown packet with opcode $opCode")
                null
            }
        }
        return packet?.also {
            it.readFrom(buffer, null)
        }
    }
}