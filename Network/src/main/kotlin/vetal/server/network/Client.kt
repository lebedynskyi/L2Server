package vetal.server.network

import vetal.server.writeDebug

abstract class Client {
    private val TAG = "Client"
    abstract val connection: ClientConnection

    open fun sendPacket(packet: SendablePacket) {
        connection.sendPacket(packet)
        writeDebug(TAG, "Send ${packet::class.java.simpleName} in Thread ${Thread.currentThread().name}")
    }

    open fun saveAndClose(reason: SendablePacket? = null) {
        connection.pendingClose = true
        connection.askClose(reason)
    }

    override fun toString(): String {
        return "Client $connection"
    }
}