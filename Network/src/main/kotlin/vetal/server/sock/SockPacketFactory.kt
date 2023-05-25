package vetal.server.sock

import java.nio.ByteBuffer

interface SockPacketFactory {
    fun parsePacket(opCode: Byte, size: Int, buffer: ByteBuffer): ReadablePacket?
}