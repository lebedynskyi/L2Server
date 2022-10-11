package vetal.server.network

import java.nio.ByteBuffer

interface PacketParser {
    fun parsePacket(buffer: ByteBuffer, sBuffer: StringBuffer, size: Int): ReceivablePacket?
}