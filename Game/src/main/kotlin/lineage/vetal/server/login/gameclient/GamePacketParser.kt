package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.client.PacketParser
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.gameclient.packet.client.*
import java.nio.ByteBuffer

class GamePacketParser : PacketParser {
    private val TAG = "BridgeGamePacketParser"

    override fun parsePacket(buffer: ByteBuffer, sBuffer: StringBuffer, size: Int): ReceivablePacket? {
        val opCode = buffer.get().toUByte().toInt()
        return when (opCode) {
            0x00 -> ProtocolVersion()
            0x08 -> AuthLogin()
            0x0e -> RequestCharacterTemplates()
            0x0b -> RequestCreateCharacter()
            0x0d -> RequestSelectCharacter()
            0x63 -> RequestQuestList()
            0x03 -> EnterWorld()
            0x46 -> RequestRestart()
            0xd0 -> when (buffer.get().toInt()) {
                0x08 -> RequestManorList()
                else -> {
                    writeInfo(TAG, "No second opcode for 0xd0 main opcode")
                    null
                }
            }

            else -> {
                writeDebug(TAG, "Unknown packet with opcode ${Integer.toHexString(opCode)}")
                null
            }
        }?.apply {
            readFrom(buffer, sBuffer)
        }
    }
}