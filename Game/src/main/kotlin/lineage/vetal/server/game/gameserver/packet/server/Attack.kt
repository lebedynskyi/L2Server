package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class Attack(
    private val attacker: CreatureObject,
    private val hits: List<Hit>
) : GameServerPacket() {
    override val opCode: Byte = 0x05

    override fun write() {
        writeD(attacker.objectId)
        writeD(hits[0].target.objectId)
        writeD(hits[0].damage)
        writeC(hits[0].flags)
        writeD(attacker.position.x)
        writeD(attacker.position.y)
        writeD(attacker.position.z)
    }

    class Hit constructor(
        val target: CreatureObject,
        val damage: Int,
        miss: Boolean,
        crit: Boolean,
        shield: Boolean,
        useSS: Boolean,
        ssGrade: Int
    ) {
        companion object {
            const val FLAG_USESS: Int = 0x10
            const val FLAG_CRIT: Int = 0x20
            const val FLAG_SHIELD: Int = 0x40
            const val FLAG_MISS: Int = 0x80
        }

        internal var flags: Int = 0

        init {
            if (miss) {
                flags = FLAG_MISS
            } else {
                if (useSS) flags = FLAG_USESS or ssGrade
                if (crit) flags = flags or FLAG_CRIT
                if (shield) flags = flags or FLAG_SHIELD

//                Original Lucera dirty fix for lags on olympiad.
//                if (shld > 0 && !(target is PlayerObject && target.isOnOlympiad)) {
//                    flags = flags or FLAG_SHIELD
//                }
            }
        }
    }
}