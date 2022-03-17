package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.client.BridgeCrypt
import lineage.vetal.server.core.client.PacketParser
import lineage.vetal.server.core.server.DATA_HEADER_SIZE
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.bridgeserver.packets.client.RequestAuth
import lineage.vetal.server.login.bridgeserver.packets.client.RequestInit
import lineage.vetal.server.login.bridgeserver.packets.client.RequestUpdate
import java.nio.ByteBuffer

class BridgePacketParser(
    private val bridgeCrypt: BridgeCrypt
) : PacketParser {
    private val TAG = javaClass::class.java.name

    override fun parsePacket(buffer: ByteBuffer): ReceivablePacket? {
        if (buffer.position() >= buffer.limit()) {
            return null
        }
        val header = buffer.short
        val dataSize = header - DATA_HEADER_SIZE
        val decryptedSize = bridgeCrypt.decrypt(buffer.array(), buffer.position(), dataSize)

        return if (decryptedSize > 0) {
            parsePacketByOpCode(buffer)
        } else null
    }

    private fun parsePacketByOpCode(buffer: ByteBuffer): ReceivablePacket? {
        val packet = when (val opCode = buffer.get().toInt()) {
            0x01 -> RequestInit()
            0x02 -> RequestAuth()
            0x03 -> RequestUpdate()
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