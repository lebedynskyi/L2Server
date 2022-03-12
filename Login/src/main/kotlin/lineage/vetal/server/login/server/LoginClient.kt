package lineage.vetal.server.login.server

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.core.server.*
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.model.Account
import lineage.vetal.server.login.packets.server.Init
import java.nio.ByteBuffer

class LoginClient(
    private val crypt: LoginCrypt,
    sessionId: Int,
    clientConnection: ClientConnection
) : Client(sessionId, clientConnection) {
    private val TAG = "LoginClient"
    var loginState: LoginState = LoginState.CONNECTED
    var account: Account? = null

    fun sendInitPacket() {
        sendPacket(Init(sessionId, crypt.scrambleModules, crypt.blowFishKey))
    }

    override fun saveAndClose() {
        TODO("Not yet implemented")
    }

    override fun sendPacket(packet: SendablePacket) {
        clientConnection.sendPacket(packet)
    }

    override fun readPackets(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer) {
    }

    override fun sendPackets(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer) {
        byteBuffer.clear()
        tempBuffer.clear()

        var packetCounter = 0
        val packetIterator = clientConnection.packetsQueue.iterator()
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
        val wroteCount = clientConnection.write(byteBuffer)
        writeDebug(TAG, "Sent $packetCounter packets to ${clientConnection.clientAddress}")
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
}

enum class LoginState {
    CONNECTED
}