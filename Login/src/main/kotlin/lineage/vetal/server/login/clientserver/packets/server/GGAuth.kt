package lineage.vetal.server.login.clientserver.packets.server

import vetalll.server.sock.WriteablePacket

class GGAuth(
    private var sessionId: Int
) : WriteablePacket() {
    override val opCode: Byte = 0x0b

    override fun write() {
        writeD(sessionId)
        writeD(0x00)
        writeD(0x00)
        writeD(0x00)
        writeD(0x00)
    }
}