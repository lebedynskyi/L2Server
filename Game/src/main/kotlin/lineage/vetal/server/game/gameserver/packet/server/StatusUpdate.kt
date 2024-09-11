package lineage.vetal.server.game.gameserver.packet.server

import jdk.jshell.Snippet.Status
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class StatusUpdate(
    private val objectId: Int,
    private val attributes: List<StatusAttr>
) : GameServerPacket() {
    override val opCode = 0x0E.toByte()

    override fun write() {
        writeD(objectId);
        writeD(attributes.size)

        attributes.forEach {
            writeD(it.id)
            writeD(it.value)
        }
    }
}

class StatusAttr(
    val id: Int,
    val value: Int
) {
    companion object {
        private const val LEVEL: Int = 0x01
        private const val EXP: Int = 0x02
        private const val STR: Int = 0x03
        private const val DEX: Int = 0x04
        private const val CON: Int = 0x05
        private const val INT: Int = 0x06
        private const val WIT: Int = 0x07
        private const val MEN: Int = 0x08

        private const val CUR_HP: Int = 0x09
        private const val MAX_HP: Int = 0x0a
        private const val CUR_MP: Int = 0x0b
        private const val MAX_MP: Int = 0x0c

        private const val SP: Int = 0x0d
        private const val CUR_LOAD: Int = 0x0e
        private const val MAX_LOAD: Int = 0x0f

        private const val P_ATK: Int = 0x11
        private const val ATK_SPD: Int = 0x12
        private const val P_DEF: Int = 0x13
        private const val EVASION: Int = 0x14
        private const val ACCURACY: Int = 0x15
        private const val CRITICAL: Int = 0x16
        private const val M_ATK: Int = 0x17
        private const val CAST_SPD: Int = 0x18
        private const val M_DEF: Int = 0x19
        private const val PVP_FLAG: Int = 0x1a
        private const val KARMA: Int = 0x1b

        private const val CUR_CP: Int = 0x21
        private const val MAX_CP: Int = 0x22

        fun curHp(hp: Int) = StatusAttr(CUR_HP, hp)
        fun maxHp(maxHp: Int) = StatusAttr(MAX_HP, maxHp)
    }
}