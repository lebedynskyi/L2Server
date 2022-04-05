package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.core.server.ServerClose

abstract class Client {
    abstract val connection: ClientConnection

    open fun sendPacket(packet: SendablePacket) {
        connection.sendPacket(packet)
    }

    open fun saveAndClose(reason: SendablePacket = ServerClose.STATIC_PACKET) {
        connection.pendingClose = true
        connection.sendPacket(reason)
    }

    override fun toString(): String {
        return "Client ${connection.clientAddress.address.hostAddress}"
    }
}