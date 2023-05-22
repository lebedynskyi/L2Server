package vetal.server.network

import vetal.server.writeError
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentLinkedQueue

open class ClientConnection(
    private val socket: SocketChannel,
    private val selector: Selector,
    private val selectionKey: SelectionKey,
    private val clientAddress: InetSocketAddress,
    private val crypt: ConnectionCrypt,
    private val packetParser: PacketParser,
) {
    private val TAG = "ClientConnection"
    private val DATA_HEADER_SIZE = 2
    private val sendPacketsQueue = ConcurrentLinkedQueue<SendablePacket>()
    private val readPacketsQueue = ConcurrentLinkedQueue<ReceivablePacket>()

    @Volatile
    internal var pendingClose = false

    fun nextPacket(): ReceivablePacket? = readPacketsQueue.poll()
    fun hasNextPacket(): Boolean = readPacketsQueue.size > 0

    fun sendPacket(packet: SendablePacket) {
        if (selectionKey.isValid) {
            sendPacketsQueue.add(packet)
            selectionKey.interestOps(selectionKey.interestOps() or SelectionKey.OP_WRITE)
            selector.wakeup()
        } else {
            writeError(TAG, "Selection key is not valid. Probably closed")
        }
    }

    internal fun askClose(lastPacket: SendablePacket? = null) {
        sendPacketsQueue.clear()
        pendingClose = true
        if (lastPacket != null) {
            sendPacket(lastPacket)
        }
    }

    fun readPackets(byteBuffer: ByteBuffer, stringBuffer: StringBuffer) : Boolean{
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

    internal fun writePackets(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer) :Boolean {
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

    private fun readPacketFromBuffer(buffer: ByteBuffer, sBuffer: StringBuffer) : Boolean {
        while (buffer.position() < buffer.limit()) {
            val header = buffer.short
            val dataSize = header - DATA_HEADER_SIZE
            val decryptedSize = crypt.decrypt(buffer.array(), buffer.position(), dataSize)
            if (decryptedSize > 0) {
                val packet = packetParser.parsePacket(buffer, sBuffer, decryptedSize)
                readPacketsQueue.add(packet)
                buffer.position(header.toInt())
            } else {
                return false
            }
        }

        return true
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

    override fun toString(): String {
        return clientAddress.toString()
    }
}