package lineage.vetal.server.game.gameserver

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.gameserver.packet.client.*
import vetalll.server.sock.ReadablePacket
import vetalll.server.sock.SockPacketFactory
import java.nio.ByteBuffer

class GamePacketParser : SockPacketFactory {
    private val TAG = "GamePacketParser"

    override fun parsePacket(opCode: Byte, size: Int, buffer: ByteBuffer): ReadablePacket? {
        return when (opCode.toInt()) {
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
            0x14 -> RequestUseItem()
            0xd0 -> parsePacketFor0xD0(buffer)
            else -> null
        }
    }

    private fun parsePacketFor0xD0(buffer: ByteBuffer): ReadablePacket? {
        return when (buffer.get().toInt()) {
            0x08 -> RequestManorList()
            else -> {
                writeInfo(TAG, "No second opcode for 0xd0 main opcode")
                null
            }
        }
    }
}