package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.SendablePacket

abstract class Client {
    abstract val connection: ClientConnection

    open fun sendPacket(packet: SendablePacket) {
        connection.sendPacket(packet)
    }

    open fun saveAndClose() {
        connection.close()
    }
}