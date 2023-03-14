package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.gameserver.packet.GameServerPacket

class ManorList(
    private val availableManor: Array<String>
) : GameServerPacket() {
    override fun write() {
        writeC(0xFE)
        writeH(0x1B)
        writeD(availableManor.size)
        availableManor.forEachIndexed { i, s ->
            writeD(i + 1)
            writeS(s)
        }
    }
}