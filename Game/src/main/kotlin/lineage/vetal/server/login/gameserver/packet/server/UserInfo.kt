package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.game.model.position.Position
import lineage.vetal.server.login.game.model.player.PlayerObject
import lineage.vetal.server.login.game.model.player.Paperdoll
import lineage.vetal.server.login.gameserver.packet.GameServerPacket

class UserInfo(
    private val player: PlayerObject
) : GameServerPacket() {
    // Siege status. I think it will show flag or something similar
    val relation = 0

    override fun write() {
        writeC(0x04)
        writeD(player.position.x)
        writeD(player.position.y)
        writeD(player.position.z)
        writeD(player.position.heading)
        writeD(player.objectId)
        writeS(
            player.name // if (player.getPolymorphTemplate() != null) player.getPolymorphTemplate().getName() else player.getName()
        )
        writeD(player.raceId)
        writeD(player.appearance.sex.id)
        writeD(player.classId)

        writeD(player.status.level)
        writeQ(player.status.exp)
        writeD(player.status.getSTR())
        writeD(player.status.getDEX())
        writeD(player.status.getCON())
        writeD(player.status.getINT())
        writeD(player.status.getWIT())
        writeD(player.status.getMEN())
        writeD(player.status.maxHp)
        writeD(player.status.curHp.toInt())
        writeD(player.status.maxMp)
        writeD(player.status.curMp.toInt())
        writeD(player.status.sp)
        writeD(player.inventory.currentWeight)
        writeD(player.inventory.weightLimit)
        writeD(20)//writeD(if (player.getActiveWeaponItem() != null) 40 else 20)

        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.HAIRALL))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.REAR))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.LEAR))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.NECK))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.RFINGER))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.LFINGER))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.HEAD))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.RHAND))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.LHAND))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.GLOVES))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.CHEST))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.LEGS))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.FEET))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.CLOAK))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.RHAND))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.HAIR))
        writeD(player.inventory.getItemObjectIdFrom(Paperdoll.FACE))

        writeD(player.inventory.getItemIdFrom(Paperdoll.HAIRALL))
        writeD(player.inventory.getItemIdFrom(Paperdoll.REAR))
        writeD(player.inventory.getItemIdFrom(Paperdoll.LEAR))
        writeD(player.inventory.getItemIdFrom(Paperdoll.NECK))
        writeD(player.inventory.getItemIdFrom(Paperdoll.RFINGER))
        writeD(player.inventory.getItemIdFrom(Paperdoll.LFINGER))
        writeD(player.inventory.getItemIdFrom(Paperdoll.HEAD))
        writeD(player.inventory.getItemIdFrom(Paperdoll.RHAND))
        writeD(player.inventory.getItemIdFrom(Paperdoll.LHAND))
        writeD(player.inventory.getItemIdFrom(Paperdoll.GLOVES))
        writeD(player.inventory.getItemIdFrom(Paperdoll.CHEST))
        writeD(player.inventory.getItemIdFrom(Paperdoll.LEGS))
        writeD(player.inventory.getItemIdFrom(Paperdoll.FEET))
        writeD(player.inventory.getItemIdFrom(Paperdoll.CLOAK))
        writeD(player.inventory.getItemIdFrom(Paperdoll.RHAND))
        writeD(player.inventory.getItemIdFrom(Paperdoll.HAIR))
        writeD(player.inventory.getItemIdFrom(Paperdoll.FACE))

        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeD(player.inventory.getAugmentationIdFrom(Paperdoll.RHAND))
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeD(player.inventory.getAugmentationIdFrom(Paperdoll.LHAND))
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)

        writeD(player.status.getPAtk(null))
        writeD(player.status.getPAtkSpd())
        writeD(player.status.getPDef(null))
        writeD(player.status.getEvasionRate(null))
        writeD(player.status.getAccuracy())
        writeD(player.status.getCriticalHit(null, null))
        writeD(player.status.getMAtk(null, null))
        writeD(player.status.getMAtkSpd())
        writeD(player.status.getPAtkSpd())
        writeD(player.status.getMDef(null, null))
        writeD(player.pvpFlag)
        writeD(player.karma)

        val runSpd: Int = player.status.getBaseRunSpeed()
        val walkSpd: Int = player.status.getBaseWalkSpeed()
        val swimSpd: Int = player.status.getBaseSwimSpeed()

        writeD(runSpd)
        writeD(walkSpd)
        writeD(swimSpd)
        writeD(swimSpd)
        writeD(0)
        writeD(0)
        writeD(if (player.isFlying) runSpd else 0)
        writeD(if (player.isFlying) walkSpd else 0)

        writeF(player.status.getMovementSpeedMultiplier())
        writeF(player.status.getAttackSpeedMultiplier())

//        val summon: Summon = player.summon
//        if (player.isMounted && summon != null) {
//            writeF(summon.getCollisionRadius())
//            writeF(summon.getCollisionHeight())
//        } else {
        writeF(player.getCollisionRadius())
        writeF(player.getCollisionHeight())
//        }

        writeD(player.appearance.hairStyle)
        writeD(player.appearance.hairColor)
        writeD(player.appearance.face)
        writeD(if (player.isGM) 1 else 0)

        writeS(player.title) // writeS(if (player.getPolymorphTemplate() != null) "Morphed" else player.getTitle())


        writeD(player.clanId)
        writeD(player.clanCrestId)
        writeD(player.allyId)
        writeD(player.allyCrestId)
        writeD(relation)
        writeC(player.mountType)
        writeC(player.operateType.id)
        writeC(if (player.hasDwarvenCraft) 1 else 0)
        writeD(player.pkKills)
        writeD(player.pvpKills)

        writeH(0) // writeH(player.getCubicList().size())
//        for (cubic in player.getCubicList()) {
//            writeH(cubic.getId())
//        }


        writeC(if (player.isInPartyMatchRoom) 1 else 0)
        writeD(0) // writeD(player.getAbnormalEffect()) // Body effect. Big head? Stealth? Etc

        writeC(0x00)
        writeD(player.clanPrivelegies)
        writeH(player.recomLeft)
        writeH(player.recomHave)
        writeD(if (player.mountNpcId > 0) player.mountNpcId + 1000000 else 0)
        writeH(player.inventory.inventoryLimit)
        writeD(player.classId)
        writeD(0x00)
        writeD(player.status.maxCp)
        writeD(player.status.curCp.toInt())
        writeC(if (player.isMounted) 0 else player.enchantEffect)
        writeC(player.team.id)

        writeD(0) // writeD(player.getClanCrestLargeId())  // clan related stuff

        writeC(if (player.isNoble) 1 else 0)
        writeC(if (player.isHero) 1 else 0)
        writeC(if (player.isFishing) 1 else 0)
        writeLoc(Position(0, 0, 0))//writeLoc(player.getFishingStance().getLoc())
        writeD(player.appearance.nameColor)
        writeC(if (player.status.isRunning) 0x01 else 0x00)
        writeD(player.pledgeClass)
        writeD(player.pledgeType)
        writeD(player.appearance.titleColor)
        writeD(0) // writeD(CursedWeaponManager.getInstance().getCurrentStage(player.getCursedWeaponEquippedId()))
    }
}