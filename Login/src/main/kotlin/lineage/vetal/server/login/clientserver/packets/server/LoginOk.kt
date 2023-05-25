package lineage.vetal.server.login.clientserver.packets.server

import vetal.server.sock.WriteablePacket

class LoginOk(
    private val loginOk1: Int,
    private val loginOk2: Int
) : WriteablePacket() {
    override val opCode: Byte = 0x03

    override fun write() {
        writeD(loginOk1)
        writeD(loginOk2)
        writeD(0x00)
        writeD(0x00)
        writeD(0x000003ea)
        writeD(0x00)
        writeD(0x00)
        writeD(0x00)
        writeB(ByteArray(16))
    }
}