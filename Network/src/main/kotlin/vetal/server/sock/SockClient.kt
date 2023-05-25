package vetal.server.sock

import java.util.*

abstract class SockClient {
    abstract val connection: SockConnection
    val clientId = UUID.randomUUID()

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