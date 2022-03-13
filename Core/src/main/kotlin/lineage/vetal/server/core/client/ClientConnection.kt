package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.server.SendablePacket
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentLinkedQueue

abstract class ClientConnection(
    private val socket: SocketChannel,
    private val selectionKey: SelectionKey,
    val clientAddress: InetSocketAddress
) {
    protected val packetsQueue = ConcurrentLinkedQueue<SendablePacket>()

    abstract fun readData(byteBuffer: ByteBuffer, stringBuffer: StringBuffer): ReceivablePacket?
    abstract fun writeData(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer)

    fun sendPacket(packet: SendablePacket) {
        packetsQueue.add(packet)
        selectionKey.interestOps(selectionKey.interestOps() or SelectionKey.OP_WRITE)
    }

    protected fun read(writeBuffer: ByteBuffer): Int {
        return socket.read(writeBuffer)
    }

    protected fun write(writeBuffer: ByteBuffer): Int {
        return socket.write(writeBuffer)
    }

    fun close() {
        socket.close()
    }
}