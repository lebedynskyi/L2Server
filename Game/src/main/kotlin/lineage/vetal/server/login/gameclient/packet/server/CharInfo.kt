package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.model.location.Location
import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.login.game.model.player.Paperdoll
import lineage.vetal.server.login.game.model.player.Player

class CharInfo(
    private val player: Player
) : SendablePacket() {

    override fun write() {
        writeC(0x03)
        writeD(player.position.x)
        writeD(player.position.y)
        writeD(player.position.z)
        writeD(0)//writeD(if (_player.getBoat() == null) 0 else _player.getBoat().getObjectId())
        writeD(player.objectId)
        writeS(player.name)
        writeD(player.raceId)
        writeD(player.appearance.sex.id)
        writeD(player.classId)

        writeD(player.inventory.getItemIdFrom(Paperdoll.HAIRALL))
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

        writeD(player.pvpFlag)
        writeD(player.karma)
        writeD(player.status.getMAtkSpd())
        writeD(player.status.getPAtkSpd())
        writeD(player.pvpFlag)
        writeD(player.karma)

        val runSpd: Int = player.status.getBaseRunSpeed()
        val walkSpd: Int = player.status.getBaseWalkSpeed()
        val swimSpd: Int = player.status.getBaseSwimSpeed()

        writeD(runSpd)
        writeD(walkSpd)
        writeD(swimSpd)
        writeD(swimSpd)
        writeD(runSpd)
        writeD(walkSpd)
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
        writeS(player.title)
        writeD(player.clanId)
        writeD(player.clanCrestId)
        writeD(player.allyId)
        writeD(player.allyCrestId)

        writeD(0)

        writeC(if (player.isSitting) 0 else 1)
        writeC(if (player.isRunning) 1 else 0)
        writeC(if (player.isInCombat) 1 else 0)
        writeC(if (player.isAlikeDead) 1 else 0)
        writeC(0)//writeC(if (!canSeeInvis && !player.appearance.isVisible) 1 else 0)

        writeC(player.mountType)
        writeC(player.operateType.id)

        writeH(0)//writeH(player.getCubicList().size())
//        for (cubic in _player.getCubicList()) {
//            writeH(cubic.getId())
//        }

        writeC(if (player.isInPartyMatchRoom) 1 else 0)
        writeD(0)//writeD(if (canSeeInvis) _player.getAbnormalEffect() or AbnormalEffect.STEALTH.getMask() else _player.getAbnormalEffect())
        writeC(player.recomLeft)
        writeH(player.recomHave)
        writeD(player.classId)
        writeD(player.status.maxCp)
        writeD(player.status.cp.toInt())
        writeC(if (player.isMounted) 0 else player.enchantEffect)
        writeC(player.team.id)
        writeD(0) // writeD(player.getClanCrestLargeId())  // clan related stuff
        writeC(if (player.isNoble) 1 else 0)
        writeC(if (player.isHero) 1 else 0)
        writeC(if (player.isFishing) 1 else 0)
        writeLoc(Location(0, 0, 0))//writeLoc(player.getFishingStance().getLoc())
        writeD(player.appearance.nameColor)
        writeD(player.position.heading)
        writeD(player.pledgeClass)
        writeD(player.pledgeType)
        writeD(player.appearance.titleColor)
        writeD(0)//writeD(CursedWeaponManager.getInstance().getCurrentStage(_player.getCursedWeaponEquippedId()))
    }
}