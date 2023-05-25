package lineage.vetal.server.login.bridgeserver.packets.server

import vetal.server.sock.WriteablePacket

class UpdateOk : WriteablePacket() {
    override val opCode: Byte = 0x03
    override fun write() {
        writeD(1)
    }
}