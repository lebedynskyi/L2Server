package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.server.SendablePacket
import java.nio.ByteBuffer

abstract class Client(
    val clientConnection: ClientConnection,
    val crypt: ClientCrypt
) {
    abstract fun readPacket(packet: ReceivablePacket)
    abstract fun sendPacket(packet: SendablePacket)
    abstract fun sendPackets(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer)
}