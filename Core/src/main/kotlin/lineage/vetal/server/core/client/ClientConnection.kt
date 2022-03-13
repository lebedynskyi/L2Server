package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.SendablePacket
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentLinkedQueue

class ClientConnection(
    private val socket: SocketChannel,
    private val selectionKey: SelectionKey,
    val clientAddress: InetSocketAddress
) {
    val packetsQueue = ConcurrentLinkedQueue<SendablePacket>()

    fun sendPacket(packet: SendablePacket) {
        packetsQueue.add(packet)
        selectionKey.interestOps(selectionKey.interestOps() or SelectionKey.OP_WRITE)
    }

    override fun toString(): String {
        return socket.toString()
    }

    fun read(writeBuffer: ByteBuffer): Int {
        return socket.read(writeBuffer)
    }

    fun write(writeBuffer: ByteBuffer): Int {
        return socket.write(writeBuffer)
    }

    fun close() {
        socket.close()
    }
}