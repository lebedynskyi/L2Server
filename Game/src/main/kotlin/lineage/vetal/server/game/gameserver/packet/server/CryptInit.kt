package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class CryptInit(
    private val key: ByteArray
) : GameServerPacket() {
    override val opCode: Byte = 0x00

    override fun write() {
        writeC(0x01)
        writeB(key)
        writeD(0x01)
        writeD(0x01)
    }
}