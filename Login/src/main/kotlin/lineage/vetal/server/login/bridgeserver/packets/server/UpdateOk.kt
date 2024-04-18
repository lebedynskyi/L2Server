package lineage.vetal.server.login.bridgeserver.packets.server

import vetalll.server.sock.WriteablePacket

class UpdateOk : WriteablePacket() {
    override val opCode: Byte = 0x03
    override fun write() {
        writeD(1)
    }
}