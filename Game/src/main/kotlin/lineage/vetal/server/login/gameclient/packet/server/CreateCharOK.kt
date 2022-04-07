package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.login.gameclient.packet.GameServerPacket

class CreateCharOK private constructor() : GameServerPacket() {
    companion object {
        val STATIC_PACKET = CreateCharOK()
    }

    override fun write() {
        writeC(0x19)
        writeD(0x01)
    }
}