package lineage.vetal.server.game.gameserver

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.gameserver.packet.client.*
import vetal.server.network.PacketParser
import vetal.server.network.ReceivablePacket
import java.nio.ByteBuffer

class GamePacketParser : PacketParser {
    private val TAG = "GamePacketParser"

    override fun parsePacket(buffer: ByteBuffer, sBuffer: StringBuffer, size: Int): ReceivablePacket? {
        val opCode = buffer.get().toUByte().toInt()
        val packet =  when (opCode) {
            0x00 -> RequestProtocolVersion()
            0x01 -> RequestMoveToLocation()
            0x08 -> RequestAuthLogin()
            0x09 -> RequestQuit()
            0x0e -> RequestCharacterTemplates()
            0x0b -> RequestCreateCharacter()
            0x0d -> RequestSelectCharacter()
            0x63 -> RequestQuestList()
            0x03 -> RequestEnterWorld()
            0x46 -> RequestRestart()
            0x38 -> RequestSay2()
            0x48 -> ValidatePosition()
            0x12 -> RequestDropItem()
            0x04 -> {
                writeDebug(TAG, "Select target")
                null
            }
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
        }

        packet?.readFromBuffer(buffer, sBuffer)
        return packet
    }
}