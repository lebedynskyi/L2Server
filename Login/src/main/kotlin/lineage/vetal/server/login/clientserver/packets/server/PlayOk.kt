package lineage.vetal.server.login.clientserver.packets.server

import vetal.server.sock.WriteablePacket

class PlayOk(
    private val loginOk1: Int,
    private val loginOk2: Int
) : WriteablePacket() {
    override val opCode: Byte = 0x07

    override fun write() {
        writeD(loginOk1)
        writeD(loginOk2)
    }
}