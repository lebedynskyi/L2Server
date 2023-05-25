package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class ManorList(
    private val availableManor: Array<String>
) : GameServerPacket() {
    override val opCode: Byte = 0xFE.toByte()

    override fun write() {
        writeH(0x1B)
        writeD(availableManor.size)
        availableManor.forEachIndexed { i, s ->
            writeD(i + 1)
            writeS(s)
        }
    }
}