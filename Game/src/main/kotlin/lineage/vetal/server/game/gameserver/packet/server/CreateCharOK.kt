package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class CreateCharOK private constructor() : GameServerPacket() {
    companion object {
        val STATIC_PACKET = CreateCharOK()
    }

    override fun write() {
        writeC(0x19)
        writeD(0x01)
    }
}