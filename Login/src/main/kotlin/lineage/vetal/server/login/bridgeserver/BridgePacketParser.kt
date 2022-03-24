package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.client.PacketParser
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.bridgeserver.packets.client.RequestAuth
import lineage.vetal.server.login.bridgeserver.packets.client.RequestInit
import lineage.vetal.server.login.bridgeserver.packets.client.RequestUpdate
import java.nio.ByteBuffer

class BridgePacketParser : PacketParser {
    private val TAG = "BridgePacketParser"

    override fun parsePacket(buffer: ByteBuffer, decryptedSize: Int): ReceivablePacket? {
        val packet = when (val opCode = buffer.get().toInt()) {
            0x01 -> RequestInit()
            0x02 -> RequestAuth()
            0x03 -> RequestUpdate()
            else -> {
                writeDebug(TAG, "Unknown packet with opcode $opCode")
                null
            }
        }
        return packet?.also {
            it.readFrom(buffer, null)
        }
    }
}