package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.server.SendablePacket

class CreateCharOK private constructor() : SendablePacket() {
    companion object {
        val STATIC_PACKET = CreateCharOK()
    }

    override fun write() {
        writeC(0x19)
        writeD(0x01)
    }
}