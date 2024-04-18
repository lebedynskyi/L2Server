package lineage.vetal.server.game.bridgeclient

import lineage.vetal.server.game.bridgeclient.packets.server.AuthOk
import lineage.vetal.server.game.bridgeclient.packets.server.InitOK
import lineage.vetal.server.game.bridgeclient.packets.server.UpdateOk
import vetalll.server.sock.ReadablePacket
import vetalll.server.sock.SockPacketFactory
import java.nio.ByteBuffer

class BridgeGamePacketParser : SockPacketFactory {
    override fun parsePacket(opCode: Byte, size: Int, buffer: ByteBuffer): ReadablePacket? {
        return when (opCode.toInt()) {
            0x01 -> InitOK()
            0x02 -> AuthOk()
            0x03 -> UpdateOk()
            else -> null
        }
    }
}