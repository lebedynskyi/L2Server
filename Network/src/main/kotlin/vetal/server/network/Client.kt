package vetal.server.network

import java.util.UUID

abstract class Client {
    abstract val connection: ClientConnection
    val clientId = UUID.randomUUID()

    open fun sendPacket(packet: SendablePacket) {
        connection.sendPacket(packet)
    }

    open fun saveAndClose(reason: SendablePacket? = null) {
        connection.pendingClose = true
        connection.askClose(reason)
    }

    override fun toString(): String {
        return "Client: $connection"
    }
}