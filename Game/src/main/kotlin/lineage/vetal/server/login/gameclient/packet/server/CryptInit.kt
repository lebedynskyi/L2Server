package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.login.gameclient.packet.GameServerPacket

class CryptInit(
    private val key: ByteArray
) : GameServerPacket() {

    override fun write() {
        writeC(0x00)
        writeC(0x01)
        writeB(key)
        writeD(0x01)
        writeD(0x01)
    }
}