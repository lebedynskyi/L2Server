package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class CreateCharOK private constructor() : GameServerPacket() {
    override val opCode: Byte = 0x19

    override fun write() {
        writeD(0x01)
    }

    companion object {
        val STATIC_PACKET = CreateCharOK()
    }
}