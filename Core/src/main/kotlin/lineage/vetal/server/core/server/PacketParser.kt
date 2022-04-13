package lineage.vetal.server.core.server

import java.nio.ByteBuffer

interface PacketParser {
    fun parsePacket(buffer: ByteBuffer, sBuffer: StringBuffer, size: Int): ReceivablePacket?
}