package lineage.vetal.server.core.client

import lineage.vetal.server.core.server.DATA_HEADER_SIZE
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentLinkedQueue

open class ClientConnection(
    val socket: SocketChannel,
    val selectionKey: SelectionKey,
    val clientAddress: InetSocketAddress,
    private val crypt: ClientCrypt,
    private val packetParser: PacketParser,
) {
    private val TAG = javaClass::class.java.name
    private val packetsQueue = ConcurrentLinkedQueue<SendablePacket>()

    @Volatile
    var pendingClose = false

    fun readData(byteBuffer: ByteBuffer, stringBuffer: StringBuffer): ReceivablePacket? {
        byteBuffer.clear()
        stringBuffer.setLength(0)

        val readResult = read(byteBuffer)
        if (readResult <= 0) {
            return null
        }

        byteBuffer.flip()
        return readPacketsFromBuffer(byteBuffer)
    }

    fun writeData(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer) {
        byteBuffer.clear()
        tempBuffer.clear()

        var packetCounter = 0
        val packetIterator = packetsQueue.iterator()
        while (packetIterator.hasNext()) {
            packetCounter += 1
            val packet = packetIterator.next()

            // TODO check is packet was written to buffer or not.. It could be not enough space
            writePacketToBuffer(packet, tempBuffer)
            tempBuffer.flip()
            byteBuffer.put(tempBuffer)

            packetIterator.remove()
        }
        byteBuffer.flip()
        val wroteCount = write(byteBuffer)
        writeDebug(TAG, "Sent $packetCounter packets to $clientAddress data size $wroteCount")
    }

    fun readPacketsFromBuffer(byteBuffer: ByteBuffer): ReceivablePacket? {
        return packetParser.parsePacket(byteBuffer)
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

    fun sendPacket(packet: SendablePacket) {
        packetsQueue.add(packet)
        try {
            selectionKey.interestOps(selectionKey.interestOps() or SelectionKey.OP_WRITE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun read(writeBuffer: ByteBuffer): Int {
        return socket.read(writeBuffer)
    }

    private fun write(writeBuffer: ByteBuffer): Int {
        return socket.write(writeBuffer)
    }

    fun close() {
        pendingClose = true
    }
}