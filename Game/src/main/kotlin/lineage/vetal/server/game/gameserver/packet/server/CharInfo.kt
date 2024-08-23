package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.inventory.PaperDollSlot
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class CharInfo(
    private val player: PlayerObject
) : GameServerPacket() {
    override val opCode: Byte = 0x03

    override fun write() {
        writeD(player.position.x)
        writeD(player.position.y)
        writeD(player.position.z)
        writeD(0)//writeD(if (_player.getBoat() == null) 0 else _player.getBoat().getObjectId())
        writeD(player.objectId)
        writeS(player.name)
        writeD(player.template.charClass.race.ordinal)
        writeD(player.appearance.sex.id)
        writeD(player.template.charClass.ordinal)

        writeD(player.inventory.getItemIdFrom(PaperDollSlot.HAIRALL))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.HEAD))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.RHAND))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.LHAND))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.GLOVES))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.CHEST))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.LEGS))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.FEET))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.UNDERWEAR))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.RHAND))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.HAIR))
        writeD(player.inventory.getItemIdFrom(PaperDollSlot.FACE))

        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeD(player.inventory.getAugmentationIdFromSlot(PaperDollSlot.RHAND))
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
        writeD(player.inventory.getAugmentationIdFromSlot(PaperDollSlot.LHAND))
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)
        writeH(0x00)

        writeD(player.pvpFlag)
        writeD(player.karma)
        writeD(player.stats.getMAtkSpd())
        writeD(player.stats.getPAtkSpd())
        writeD(player.pvpFlag)
        writeD(player.karma)

        val runSpd: Int = player.stats.getBaseRunSpeed()
        val walkSpd: Int = player.stats.getBaseWalkSpeed()
        val swimSpd: Int = player.stats.getBaseSwimSpeed()

        writeD(runSpd)
        writeD(walkSpd)
        writeD(swimSpd)
        writeD(swimSpd)
        writeD(runSpd)
        writeD(walkSpd)
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
        writeS(player.title)
        writeD(player.clanId)
        writeD(player.clanCrestId)
        writeD(player.allyId)
        writeD(player.allyCrestId)

        writeD(0)

        writeC(if (player.stats.isSitting) 0 else 1)
        writeC(if (player.stats.isRunning) 1 else 0)
        writeC(if (player.isInCombat) 1 else 0)
        writeC(if (player.isAlikeDead) 1 else 0)
        writeC(0)//writeC(if (!canSeeInvis && !player.appearance.isVisible) 1 else 0)

        writeC(player.stats.mountType)
        writeC(player.operateType.id)

        writeH(0)//writeH(player.getCubicList().size())
//        for (cubic in _player.getCubicList()) {
//            writeH(cubic.getId())
//        }

        writeC(if (player.isInPartyMatchRoom) 1 else 0)
        writeD(0)//writeD(if (canSeeInvis) _player.getAbnormalEffect() or AbnormalEffect.STEALTH.getMask() else _player.getAbnormalEffect())
        writeC(player.recLeft)
        writeH(player.recHave)
        writeD(player.template.charClass.ordinal)
        writeD(player.stats.maxCp)
        writeD(player.stats.curCp.toInt())
        writeC(if (player.stats.isMounted) 0 else player.enchantEffect)
        writeC(player.team.id)
        writeD(0) // writeD(player.getClanCrestLargeId())  // clan related stuff
        writeC(if (player.isNoble) 1 else 0)
        writeC(if (player.isHero) 1 else 0)
        writeC(if (player.stats.isFishing) 1 else 0)
        writeLoc(Position(0, 0, 0))//writeLoc(player.getFishingStance().getLoc())
        writeD(player.appearance.nameColor)
        writeD(player.position.heading)
        writeD(player.pledgeClass)
        writeD(player.pledgeType)
        writeD(player.appearance.titleColor)
        writeD(0)//writeD(CursedWeaponManager.getInstance().getCurrentStage(_player.getCursedWeaponEquippedId()))
    }
}