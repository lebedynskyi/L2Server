package vetal.server.sock

import java.util.*

open class SockClient(
    open val connection: SockConnection,
    val clientId: UUID? = UUID.randomUUID()
) {
    open fun sendPacket(packet: WriteablePacket) {
        connection.sendPacket(packet)
    }

    open fun saveAndClose(reason: WriteablePacket? = null) {
        connection.pendingClose = true
        connection.askClose(reason)
    }

    override fun toString(): String {
        return "Client: $connection"
    }
}