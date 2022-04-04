package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.server.SendablePacket

class ManorList : SendablePacket() {
    override fun write() {
        writeC(0xFE)
        writeH(0x1B)
        writeD(MANORS.size)
        for (i in MANORS.indices) {
            writeD(i + 1)
            writeS(MANORS[i])
        }
    }

    companion object {
        private val MANORS = arrayOf(
            "gludio",
            "dion",
            "giran",
            "oren",
            "aden",
            "innadril",
            "goddard",
            "rune",
            "schuttgart"
        )
        val STATIC_PACKET = ManorList()
    }
}