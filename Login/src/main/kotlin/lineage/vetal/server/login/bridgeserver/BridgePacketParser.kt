package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.bridgeserver.packets.client.RequestAuth
import lineage.vetal.server.login.bridgeserver.packets.client.RequestInit
import lineage.vetal.server.login.bridgeserver.packets.client.RequestUpdate
import vetalll.server.sock.SockPacketFactory
import vetalll.server.sock.ReadablePacket
import java.nio.ByteBuffer

class BridgePacketParser : SockPacketFactory {
    private val TAG = "BridgePacketParser"

    override fun parsePacket(opCode: Byte, size: Int, buffer: ByteBuffer): ReadablePacket? {
        return when (opCode.toInt()) {
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