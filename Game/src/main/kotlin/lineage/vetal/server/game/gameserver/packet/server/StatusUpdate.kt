package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class StatusUpdate(
    private val objectId: Int,
    private val attributes: List<Pair<Int, Int>>
) : GameServerPacket() {
    override val opCode = 0x0E.toByte()

    override fun write() {
        writeD(objectId);
        writeD(attributes.size)

        attributes.forEach {
            writeD(it.first)
            writeD(it.second)
        }
    }
}

class StatusAttribute(
    val id: Int,
    val value: Int
) {
    companion object {
        const val LEVEL: Int = 0x01
        const val EXP: Int = 0x02
        const val STR: Int = 0x03
        const val DEX: Int = 0x04
        const val CON: Int = 0x05
        const val INT: Int = 0x06
        const val WIT: Int = 0x07
        const val MEN: Int = 0x08

        const val CUR_HP: Int = 0x09
        const val MAX_HP: Int = 0x0a
        const val CUR_MP: Int = 0x0b
        const val MAX_MP: Int = 0x0c

        const val SP: Int = 0x0d
        const val CUR_LOAD: Int = 0x0e
        const val MAX_LOAD: Int = 0x0f

        const val P_ATK: Int = 0x11
        const val ATK_SPD: Int = 0x12
        const val P_DEF: Int = 0x13
        const val EVASION: Int = 0x14
        const val ACCURACY: Int = 0x15
        const val CRITICAL: Int = 0x16
        const val M_ATK: Int = 0x17
        const val CAST_SPD: Int = 0x18
        const val M_DEF: Int = 0x19
        const val PVP_FLAG: Int = 0x1a
        const val KARMA: Int = 0x1b

        const val CUR_CP: Int = 0x21
        const val MAX_CP: Int = 0x22

        fun curHp(hp: Int): Pair<Int, Int> = CUR_HP to hp
        fun maxHp(maxHp: Int): Pair<Int, Int> = MAX_HP to maxHp
    }
}