package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.SendablePacket

abstract class Client {
    abstract val connection: ClientConnection

    open fun sendPacket(packet: SendablePacket) {
        connection.sendPacket(packet)
    }

    open fun saveAndClose(reason: SendablePacket? = null) {
        connection.close()
        reason?.let {
            connection.sendPacket(reason)
        }
    }

    override fun toString(): String {
        return "Client ${connection.clientAddress.address.hostAddress}"
    }
}