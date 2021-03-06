package lineage.vetal.server.login.clientserver.packets.server

import lineage.vetal.server.core.server.SendablePacket

class PlayOk(
    val loginOk1: Int,
    val loginOk2: Int
) : SendablePacket() {
    override fun write() {
        writeC(0x07)
        writeD(loginOk1)
        writeD(loginOk2)
    }
}