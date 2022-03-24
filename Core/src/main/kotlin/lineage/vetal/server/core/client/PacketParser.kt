package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.ReceivablePacket
import java.nio.ByteBuffer

interface PacketParser {
    fun parsePacket(buffer: ByteBuffer, decryptedSize: Int): ReceivablePacket?
}