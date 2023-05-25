package lineage.vetal.server.login.bridgeserver.packets.server

import vetal.server.sock.WriteablePacket

class InitOK : WriteablePacket() {
    override val opCode: Byte = 0x01

    override fun write() {
        writeD(1)
    }
}