package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class NpcInfo(
    private val npc: NpcObject
) : GameServerPacket() {
    override fun write() {
        writeC(0x16)

        writeD(npc.objectId)
        writeD(npc.template.idTemplate + 1000000)
        writeD(if (true) 1 else 0) // writeD(_isAttackable ? 1 : 0);

        writeD(npc.position.x)
        writeD(npc.position.y)
        writeD(npc.position.z)
        writeD(npc.position.heading)

        writeD(0x00)

        writeD(npc.stats.getMAtkSpd())
        writeD(npc.stats.getPAtkSpd())
        writeD(npc.stats.getBaseRunSpeed())
        writeD(npc.stats.getBaseWalkSpeed())
        writeD(npc.stats.getBaseRunSpeed())
        writeD(npc.stats.getBaseWalkSpeed())
        writeD(npc.stats.getBaseRunSpeed())
        writeD(npc.stats.getBaseWalkSpeed())
        writeD(npc.stats.getBaseRunSpeed())
        writeD(npc.stats.getBaseWalkSpeed())

        writeF(npc.stats.getMovementSpeedMultiplier())
        writeF(npc.stats.getAttackSpeedMultiplier())

        writeF(npc.template.collisionRadius)
        writeF(npc.template.collisionHeight)

        writeD(0)//writeD(_rhand)
        writeD(0)//writeD(_chest)
        writeD(0)//writeD(_lhand)

        writeC(1) // name above char

        writeC(if (npc.isRunning) 1 else 0)
        writeC(if (npc.isInCombat) 1 else 0)
        writeC(if (npc.isAlikeDead) 1 else 0)
        writeC(0)//writeC(if (_isSummoned) 2 else 0)

        writeS(npc.name)
        writeS(npc.title)

        writeD(0x00)
        writeD(0x00)
        writeD(0x00)

        writeD(0)//writeD(npc.getAbnormalEffect())

        writeD(0)//writeD(_clanId)
        writeD(0)//writeD(_clanCrest)
        writeD(0)//writeD(_allyId)
        writeD(0)//writeD(_allyCrest)

        writeC(0)//writeC(npc.getMove().getMoveType().getId())
        writeC(0x00)

        writeF(npc.template.collisionRadius)
        writeF(npc.template.collisionHeight)

        writeD(0)//writeD(_enchantEffect)
        writeD(if (npc.isFlying) 1 else 0)
    }
}