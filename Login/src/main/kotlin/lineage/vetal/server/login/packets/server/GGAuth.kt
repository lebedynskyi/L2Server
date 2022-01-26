package lineage.vetal.server.login.packets.server

import lineage.vetal.server.core.server.SendablePacket


class GGAuth(
    var sessionId: Int
) : SendablePacket() {
    override fun write() {
        writeC(0x0b)
        writeD(sessionId)
        writeD(0x00)
        writeD(0x00)
        writeD(0x00)
        writeD(0x00)
    }
}