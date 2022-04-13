package lineage.vetal.server.core.server

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentLinkedQueue

open class ClientConnection(
    val socket: SocketChannel,
    val selector: Selector,
    val selectionKey: SelectionKey,
    val clientAddress: InetSocketAddress,
    private val crypt: ClientCrypt,
    private val packetParser: PacketParser,
) {
    private val TAG = "ClientConnection"
    private val DATA_HEADER_SIZE = 2
    private val packetsQueue = ConcurrentLinkedQueue<SendablePacket>()

    @Volatile
    internal var pendingClose = false

    fun sendPacket(packet: SendablePacket) {
        packetsQueue.add(packet)
        selectionKey.interestOps(selectionKey.interestOps() or SelectionKey.OP_WRITE)
        selector.wakeup()
    }

    internal fun askClose(lastPacket: SendablePacket? = null) {
        packetsQueue.clear()
        pendingClose = true
        if (lastPacket != null) {
            sendPacket(lastPacket)
        }
    }

    fun readPackets(byteBuffer: ByteBuffer, stringBuffer: StringBuffer): ReceivablePacket? {
        byteBuffer.clear()
        stringBuffer.setLength(0)

        val readResult = readData(byteBuffer)
        if (readResult <= 0) {
            // data < 0 means connection closed
            pendingClose = true
            return null
        }

        byteBuffer.flip()
        return readPacketFromBuffer(byteBuffer, stringBuffer)
    }

    internal fun writePackets(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer) {
        byteBuffer.clear()

        var packetCounter = 0
        val packetIterator = packetsQueue.iterator()
        while (packetIterator.hasNext()) {
            tempBuffer.clear()
            packetCounter += 1
            val packet = packetIterator.next()

            // TODO check is packet was written to buffer or not.. It could be not enough space
            // temp buffer is needed to check is enough capacity in byte buffer
            writePacketToBuffer(packet, tempBuffer)
            tempBuffer.flip()
            byteBuffer.put(tempBuffer)
            packetIterator.remove()
        }

        // TODO check is packet was written to buffer or not.. It could be not enough space
        byteBuffer.flip()
        val sentBytesCount = writeData(byteBuffer)

        if (packetsQueue.size <= 0) {
            selectionKey.interestOps(selectionKey.interestOps() and SelectionKey.OP_WRITE.inv())
        }
    }

    internal fun closeSocket() {
        selectionKey.cancel()
        socket.close()
    }

    private fun readPacketFromBuffer(buffer: ByteBuffer, sBuffer: StringBuffer): ReceivablePacket? {
        if (buffer.position() >= buffer.limit()) {
            return null
        }
        val header = buffer.short
        val dataSize = header - DATA_HEADER_SIZE
        val decryptedSize = crypt.decrypt(buffer.array(), buffer.position(), dataSize)

        return if (decryptedSize > 0) {
            packetParser.parsePacket(buffer, sBuffer, decryptedSize)
        } else null
    }

    private fun writePacketToBuffer(packet: SendablePacket, buffer: ByteBuffer) {
        // reserve space for the size
        buffer.position(buffer.position() + DATA_HEADER_SIZE)

        // Write packet to buffer
        val dataStartPosition = buffer.position()
        packet.writeInto(buffer)
        val dataSize = buffer.position() - dataStartPosition

        // Encrypt data exclusive header (reserved space for size of whole packet)
        val encryptedSize = crypt.encrypt(buffer.array(), dataStartPosition, dataSize)

        // Write final size to reserved header
        buffer.position(dataStartPosition - DATA_HEADER_SIZE)
        buffer.putShort((encryptedSize + DATA_HEADER_SIZE).toShort())

        // Set position to end of packet
        buffer.position(dataStartPosition + encryptedSize)
    }

    private fun readData(writeBuffer: ByteBuffer): Int {
        return socket.read(writeBuffer)
    }

    private fun writeData(writeBuffer: ByteBuffer): Int {
        return socket.write(writeBuffer)
    }
}