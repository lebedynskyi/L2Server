package lineage.vetal.server.login.bridgeclient

import lineage.vetal.server.core.client.PacketParser
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.bridgeclient.packets.server.AuthOk
import lineage.vetal.server.login.bridgeclient.packets.server.InitOK
import lineage.vetal.server.login.bridgeclient.packets.server.UpdateOk
import java.nio.ByteBuffer

class BridgeGamePacketParser : PacketParser {

    private val TAG = "BridgeGamePacketParser"

    override fun parsePacket(buffer: ByteBuffer, decryptedSize: Int): ReceivablePacket? {
        return when (val opCode = buffer.get().toInt()) {
            0x01 -> InitOK()
            0x02 -> AuthOk()
            0x03 -> UpdateOk()
            else -> {
                writeDebug(TAG, "Unknown packet with opcode $opCode")
                null
            }
        }?.apply {
            readFrom(buffer, null)
        }
    }
}