package lineage.vetal.server.login.bridgeserver.packets.server

import vetal.server.sock.WriteablePacket

class AuthOk : WriteablePacket() {
    override val opCode: Byte = 0x02
    override fun write() {
        writeD(1)
    }
}