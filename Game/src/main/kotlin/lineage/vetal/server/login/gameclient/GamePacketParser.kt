package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.client.PacketParser
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.gameclient.packet.client.*
import java.nio.ByteBuffer

class GamePacketParser : PacketParser {
    private val TAG = "BridgeGamePacketParser"

    override fun parsePacket(buffer: ByteBuffer, sBuffer: StringBuffer, size: Int): ReceivablePacket? {
        return when (val opCode = buffer.get().toInt()) {
            0x00 -> ProtocolVersion()
            0x08 -> AuthLogin()
            0x0e -> RequestCharacterTemplates()
            0x0b -> RequestCreateCharacter()
            0x0d -> RequestSelectCharacter()

            else -> {
                writeDebug(TAG, "Unknown packet with opcode ${Integer.toHexString(opCode)}")
                null
            }
        }?.apply {
            readFrom(buffer, sBuffer)
        }
    }
}