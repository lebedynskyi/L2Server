package lineage.vetal.server.login.server

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.core.server.*
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.model.Account
import lineage.vetal.server.login.packets.LoginPacketParser
import lineage.vetal.server.login.packets.server.Init
import java.nio.ByteBuffer

// TODO move read write byte interaction inside connection

class LoginClient(
    private val crypt: LoginCrypt,
    private val packetParser: LoginPacketParser,
    sessionId: Int,
    clientConnection: ClientConnection
) : Client(sessionId, clientConnection) {
    private val TAG = "LoginClient"

    var loginState: LoginState = LoginState.CONNECTED
    var account: Account? = null

    fun sendInitPacket() {
        sendPacket(Init(sessionId, crypt.scrambleModules, crypt.blowFishKey))
    }

    override fun sendPacket(packet: SendablePacket) {
        connection.sendPacket(packet)
    }

    override fun readPackets(byteBuffer: ByteBuffer, stringBuffer: StringBuffer): ReceivablePacket? {
        byteBuffer.clear()
        stringBuffer.setLength(0)

        val readResult = connection.read(byteBuffer)
        if (readResult <= 0) {
            return null
        }

        byteBuffer.flip()
        return packetParser.parsePacket(byteBuffer, stringBuffer)
    }

    override fun sendPackets(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer) {
        byteBuffer.clear()
        tempBuffer.clear()

        var packetCounter = 0
        val packetIterator = connection.packetsQueue.iterator()
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
        val wroteCount = connection.write(byteBuffer)
        writeDebug(TAG, "Sent $packetCounter packets to ${connection.clientAddress}")
    }

    override fun saveAndClose() {
        connection.close()
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

    override fun toString(): String {
        return "Client id $sessionId ip ${connection.clientAddress.address.hostAddress}"
    }
}

enum class LoginState {
    CONNECTED
}