package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.bridgeserver.packets.client.RequestAuth
import lineage.vetal.server.login.bridgeserver.packets.client.RequestInit
import lineage.vetal.server.login.bridgeserver.packets.client.RequestUpdate
import vetal.server.network.PacketParser
import vetal.server.network.ReceivablePacket
import java.nio.ByteBuffer

class BridgePacketParser : PacketParser {
    private val TAG = "BridgePacketParser"

    override fun parsePacket(buffer: ByteBuffer, sBuffer: StringBuffer, size: Int): ReceivablePacket? {
        return when (val opCode = buffer.get().toInt()) {
            0x01 -> RequestInit()
            0x02 -> RequestAuth()
            0x03 -> RequestUpdate()
            else -> {
                writeDebug(TAG, "Unknown packet with opcode $opCode")
                null
            }
        }
    }
}