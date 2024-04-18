package lineage.vetal.server.game.bridgeclient.packets.client

import vetalll.server.sock.WriteablePacket

class RequestInit(
    private var serverId: Int
) : WriteablePacket() {
    override val opCode: Byte = 0x01

    override fun write() {
        writeD(serverId)
    }
}