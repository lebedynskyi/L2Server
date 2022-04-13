package lineage.vetal.server.core.server

abstract class Client {
    abstract val connection: ClientConnection

    open fun sendPacket(packet: SendablePacket) {
        connection.sendPacket(packet)
    }

    open fun saveAndClose(reason: SendablePacket? = null) {
        connection.pendingClose = true
        connection.askClose(reason)
    }

    override fun toString(): String {
        return "Client ${connection.clientAddress.address.hostAddress}"
    }
}