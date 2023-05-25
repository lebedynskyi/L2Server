package vetal.server.sock

import vetal.server.writeError
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentLinkedQueue

open class SockConnection(
    private val socket: SocketChannel,
    private val selector: Selector,
    private val selectionKey: SelectionKey,
    private val packetParser: SockPacketFactory,
    private val crypt: SockCrypt = SockCrypt.NO_CRYPT,
) {
    private val TAG = "ClientConnection"
    private val clientAddress: InetSocketAddress = socket.remoteAddress as InetSocketAddress
    private val headerSize = 2 // 2 bytes. opcode + size
    private val sendPacketsQueue = ConcurrentLinkedQueue<WriteablePacket>()
    private val readPacketsQueue = ConcurrentLinkedQueue<ReadablePacket>()

    @Volatile
    internal var pendingClose = false

    fun nextPacket(): ReadablePacket? = readPacketsQueue.poll()
    fun hasNextPacket(): Boolean = readPacketsQueue.size > 0

    fun sendPacket(packet: WriteablePacket) {
        if (selectionKey.isValid) {
            sendPacketsQueue.add(packet)
            selectionKey.interestOps(selectionKey.interestOps() or SelectionKey.OP_WRITE)
            selector.wakeup()
        } else {
            writeError(TAG, "Selection key is not valid. Probably closed")
        }
    }

    internal fun askClose(lastPacket: WriteablePacket? = null) {
        sendPacketsQueue.clear()
        pendingClose = true
        if (lastPacket != null) {
            sendPacket(lastPacket)
        }
    }

    fun readPackets(byteBuffer: ByteBuffer, stringBuffer: StringBuffer): Boolean {
        byteBuffer.clear()
        stringBuffer.setLength(0)

        val readResult = readData(byteBuffer)
        if (readResult <= 0) {
            // data < 0 means connection closed
            pendingClose = true
            return false
        }

        byteBuffer.flip()
        return readPacketFromBuffer(byteBuffer, stringBuffer)
    }

    internal fun writePackets(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer): Boolean {
        byteBuffer.clear()

        val packetIterator = sendPacketsQueue.iterator()
        while (packetIterator.hasNext()) {
            tempBuffer.clear()
            val packet = packetIterator.next()

            // TODO check is packet was written to buffer or not.. It could be not enough space
            // temp buffer is needed to check is enough capacity in byte buffer
            // todo in genreal this checking does not work. tried to send 6k packets
            writePacketToBuffer(packet, tempBuffer)
            tempBuffer.flip()
            byteBuffer.put(tempBuffer)
            packetIterator.remove()
        }

        // TODO check is packet was written to buffer or not.. It could be not enough space in socket
        byteBuffer.flip()
        val sentBytesCount = writeData(byteBuffer)
        return sendPacketsQueue.size <= 0
    }

    internal fun closeSocket() {
        try {
            selectionKey.cancel()
            socket.close()
        } catch (e: Exception) {
            writeError(TAG, "Unable to close connection", e)
        }
    }

    private fun readPacketFromBuffer(buffer: ByteBuffer, sBuffer: StringBuffer): Boolean {
        while (buffer.position() < buffer.limit()) {
            val packetHeader = buffer.short
            val packetSize = packetHeader - headerSize
            val nextPacketPosition = buffer.position() + packetSize

            val decryptedSize = crypt.decrypt(buffer.array(), buffer.position(), packetSize)
            if (decryptedSize > 0) {
                val opCode = buffer.get()
                val packet = packetParser.parsePacket(opCode, packetSize, buffer)
                if (packet != null) {
                    packet.readFromBuffer(buffer, sBuffer)
                    readPacketsQueue.add(packet)
                }
                buffer.position(nextPacketPosition)
            } else {
                return false
            }
        }

        return true
    }

    private fun writePacketToBuffer(packet: WriteablePacket, buffer: ByteBuffer) {
        // reserve space for the size
        buffer.position(buffer.position() + headerSize)

        // Write packet to buffer
        val dataStartPosition = buffer.position()
        buffer.put(packet.opCode)
        packet.writeInto(buffer)
        val dataSize = buffer.position() - dataStartPosition

        // Encrypt data exclusive header (reserved space for size of whole packet)
        val encryptedSize = crypt.encrypt(buffer.array(), dataStartPosition, dataSize)

        // Write final size to reserved header
        buffer.position(dataStartPosition - headerSize)
        buffer.putShort((encryptedSize + headerSize).toShort())

        // Set position to end of packet
        buffer.position(dataStartPosition + encryptedSize)
    }

    private fun readData(writeBuffer: ByteBuffer): Int {
        return socket.read(writeBuffer)
    }

    private fun writeData(writeBuffer: ByteBuffer): Int {
        return socket.write(writeBuffer)
    }

    override fun toString(): String {
        return clientAddress.toString()
    }
}