package lineage.vetal.server.core.server

import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.LinkedList

class ClientConnection(
    val socket: SocketChannel,
    val selector: Selector
) {
    val packetsBuffer = LinkedList<SendablePacket>()

    fun sendPacket(packet: SendablePacket) {

    }

    override fun toString(): String {
        return socket.toString()
    }
}