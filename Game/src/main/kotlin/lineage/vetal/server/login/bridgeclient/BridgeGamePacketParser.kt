package lineage.vetal.server.login.bridgeclient

import lineage.vetal.server.core.client.BridgeCrypt
import lineage.vetal.server.core.client.PacketParser
import lineage.vetal.server.core.encryption.CryptUtil
import lineage.vetal.server.core.server.DATA_HEADER_SIZE
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.bridgeclient.packets.server.AuthOk
import lineage.vetal.server.login.bridgeclient.packets.server.InitOK
import lineage.vetal.server.login.bridgeclient.packets.server.UpdateOk
import java.nio.ByteBuffer

class BridgeGamePacketParser(
    private val bridgeGameCrypt: BridgeCrypt
) : PacketParser {

    private val TAG = "LoginClientPacketParser"

    override fun parsePacket(buffer: ByteBuffer): ReceivablePacket? {
        if (buffer.position() >= buffer.limit()) {
            return null
        }
        val header = buffer.short
        val dataSize = header - DATA_HEADER_SIZE
        val decryptedSize = bridgeGameCrypt.decrypt(buffer.array(), buffer.position(), dataSize)

        return if (decryptedSize > 0) {
            parsePacketByOpCode(buffer)
        } else null
    }

    private fun parsePacketByOpCode(buffer: ByteBuffer): ReceivablePacket? {
        val packet = when (val opCode = buffer.get().toInt()) {
            0x00 -> InitOK()
            0x01 -> AuthOk()
            0x02 -> UpdateOk()
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