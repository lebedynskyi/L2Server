package lineage.vetal.server.core.server

import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.util.LinkedList

class ClientConnection(
    val socket: SocketChannel,
    val selectionKey: SelectionKey
) {
    val packetsBuffer = LinkedList<SendablePacket>()

    fun sendPacket(packet: SendablePacket) {
        selectionKey.interestOps(SelectionKey.OP_WRITE)
    }

    override fun toString(): String {
        return socket.toString()
    }
}