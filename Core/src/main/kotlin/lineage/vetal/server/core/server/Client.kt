package lineage.vetal.server.core.server

abstract class Client(
    val clientConnection: ClientConnection
) {
    abstract fun readPacket(packet: ReceivablePacket)
    abstract fun sendPacket(packet: SendablePacket)
}