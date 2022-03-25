package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.server.SendablePacket

class CryptInit(
    private val key: ByteArray
) : SendablePacket() {

    override fun write() {
        writeC(0x00)
        writeC(0x01)
        writeB(key)
        writeD(0x01)
        writeD(0x01)
    }
}