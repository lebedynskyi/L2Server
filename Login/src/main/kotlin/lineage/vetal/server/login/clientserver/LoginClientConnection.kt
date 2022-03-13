package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.core.server.DATA_HEADER_SIZE
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.clientserver.packets.LoginPacketParser
import lineage.vetal.server.login.clientserver.packets.server.Init
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel

class LoginClientConnection(
    private val loginCrypt: LoginClientCrypt,
    private val loginPacketParser: LoginPacketParser,
    socket: SocketChannel,
    selectionKey: SelectionKey,
    clientAddress: InetSocketAddress
) : ClientConnection(socket, selectionKey, clientAddress) {
    private val TAG = "LoginClientConnection"

    fun sendInitPacket(sessionId: Int) {
        sendPacket(Init(sessionId, loginCrypt.scrambleModules, loginCrypt.blowFishKey))
    }

    override fun readData(byteBuffer: ByteBuffer, stringBuffer: StringBuffer): ReceivablePacket? {
        byteBuffer.clear()
        stringBuffer.setLength(0)

        val readResult = read(byteBuffer)
        if (readResult <= 0) {
            return null
        }

        byteBuffer.flip()
        return loginPacketParser.parsePacket(byteBuffer, stringBuffer)
    }

    override fun writeData(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer) {
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
        writeDebug(TAG, "Sent $packetCounter packets to $clientAddress")
    }

    private fun writePacketToBuffer(packet: SendablePacket, buffer: ByteBuffer) {
        // reserve space for the size
        buffer.position(buffer.position() + DATA_HEADER_SIZE)

        // Write packet to buffer
        val dataStartPosition = buffer.position()
        packet.writeInto(buffer)
        val dataSize = buffer.position() - dataStartPosition

        // Encrypt data exclusive header (reserved space for size of whole packet)
        val encryptedSize = loginCrypt.encrypt(buffer.array(), dataStartPosition, dataSize)

        // Write final size to reserved header
        buffer.position(dataStartPosition - DATA_HEADER_SIZE)
        buffer.putShort((encryptedSize + DATA_HEADER_SIZE).toShort())

        // Set position to end of packet
        buffer.position(dataStartPosition + encryptedSize)
    }
}