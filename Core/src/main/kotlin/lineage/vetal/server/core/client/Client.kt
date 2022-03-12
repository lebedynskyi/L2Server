package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.SendablePacket
import java.nio.ByteBuffer

abstract class Client(
    val sessionId: Int,
    val clientConnection: ClientConnection
) {
    abstract fun readPackets(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer)
    abstract fun sendPacket(packet: SendablePacket)
    abstract fun sendPackets(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer)
    abstract fun saveAndClose()
}