package lineage.vetal.server.game.bridgeclient

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.bridgeclient.packets.server.AuthOk
import lineage.vetal.server.game.bridgeclient.packets.server.InitOK
import lineage.vetal.server.game.bridgeclient.packets.server.UpdateOk
import vetal.server.network.PacketParser
import vetal.server.network.ReceivablePacket
import java.nio.ByteBuffer

class BridgeGamePacketParser : PacketParser {
    private val TAG = "BridgeGamePacketParser"

    override fun parsePacket(buffer: ByteBuffer, sBuffer: StringBuffer, size: Int): ReceivablePacket? {
        return when (val opCode = buffer.get().toInt()) {
            0x01 -> InitOK()
            0x02 -> AuthOk()
            0x03 -> UpdateOk()
            else -> {
                writeDebug(TAG, "Unknown packet with opcode $opCode")
                null
            }
        }
    }
}