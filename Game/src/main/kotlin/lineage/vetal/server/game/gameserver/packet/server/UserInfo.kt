package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.player.PaperDollSlot
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class UserInfo(
    private val player: PlayerObject,
    private val relation: Int = 0, // Siege status. I think it will show flag or something similar
) : GameServerPacket() {
    override val opCode: Byte = 0x04

    override fun write() {
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

        writeD(player.stats.level)
        writeQ(player.stats.exp)
        writeD(player.stats.getSTR())
        writeD(player.stats.getDEX())
        writeD(player.stats.getCON())
        writeD(player.stats.getINT())
        writeD(player.stats.getWIT())
        writeD(player.stats.getMEN())
        writeD(player.stats.maxHp)
        writeD(player.stats.curHp.toInt())
        writeD(player.stats.maxMp)
        writeD(player.stats.curMp.toInt())
        writeD(player.stats.sp)
        writeD(player.inventory.currentWeight)
        writeD(player.inventory.weightLimit)
        writeD(20)//writeD(if (player.getActiveWeaponItem() != null) 40 else 20)

        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.HAIRALL))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.REAR))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.LEAR))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.NECK))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.RFINGER))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.LFINGER))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.HEAD))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.RHAND))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.LHAND))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.GLOVES))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.CHEST))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.LEGS))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.FEET))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.CLOAK))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.RHAND))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.HAIR))
        writeD(player.inventory.getItemObjectIdFrom(PaperDollSlot.FACE))

        writeD(player.inventory.getItemIdFrom(PaperDollSlot.HAIRALL))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.REAR))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.LEAR))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.NECK))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.RFINGER))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.LFINGER))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.HEAD))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.RHAND))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.LHAND))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.GLOVES))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.CHEST))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.LEGS))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.FEET))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.CLOAK))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.RHAND))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.HAIR))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.FACE))

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
        writeD(player.inventory.getAugmentationIdFrom(PaperDollSlot.RHAND))
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
        writeD(player.inventory.getAugmentationIdFrom(PaperDollSlot.LHAND))
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)

        writeD(player.stats.getPAtk(null))
        writeD(player.stats.getPAtkSpd())
        writeD(player.stats.getPDef(null))
        writeD(player.stats.getEvasionRate(null))
        writeD(player.stats.getAccuracy())
        writeD(player.stats.getCriticalHit(null, null))
        writeD(player.stats.getMAtk(null, null))
        writeD(player.stats.getMAtkSpd())
        writeD(player.stats.getPAtkSpd())
        writeD(player.stats.getMDef(null, null))
        writeD(player.pvpFlag)
        writeD(player.karma)

        val runSpd: Int = player.stats.getBaseRunSpeed()
        val walkSpd: Int = player.stats.getBaseWalkSpeed()
        val swimSpd: Int = player.stats.getBaseSwimSpeed()

        writeD(runSpd)
        writeD(walkSpd)
        writeD(swimSpd)
        writeD(swimSpd)
        writeD(0)
        writeD(0)
        writeD(if (player.isFlying) runSpd else 0)
        writeD(if (player.isFlying) walkSpd else 0)

        writeF(player.stats.getMovementSpeedMultiplier())
        writeF(player.stats.getAttackSpeedMultiplier())

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
        writeD(player.stats.maxCp)
        writeD(player.stats.curCp.toInt())
        writeC(if (player.isMounted) 0 else player.enchantEffect)
        writeC(player.team.id)

        writeD(0) // writeD(player.getClanCrestLargeId())  // clan related stuff

        writeC(if (player.isNoble) 1 else 0)
        writeC(if (player.isHero) 1 else 0)
        writeC(if (player.isFishing) 1 else 0)
        writeLoc(Position(0, 0, 0))//writeLoc(player.getFishingStance().getLoc())
        writeD(player.appearance.nameColor)
        writeC(if (player.stats.isRunning) 0x01 else 0x00)
        writeD(player.pledgeClass)
        writeD(player.pledgeType)
        writeD(player.appearance.titleColor)
        writeD(0) // writeD(CursedWeaponManager.getInstance().getCurrentStage(player.getCursedWeaponEquippedId()))
    }
}