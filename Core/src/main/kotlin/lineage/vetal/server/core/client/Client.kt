package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.server.SendablePacket

abstract class Client(
    val clientConnection: ClientConnection,
    val crypt: ClientCrypt
) {
    abstract fun encrypt()
    abstract fun readPacket(packet: ReceivablePacket)
    abstract fun sendPacket(packet: SendablePacket)
}