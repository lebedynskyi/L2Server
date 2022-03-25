package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.model.CharSlot

class CharSlotList(
    val gameClient: GameClient,
    val slot: List<CharSlot>
) : SendablePacket() {
    override fun write() {
        writeC(0x13)
        writeD(slot.size)

        slot.forEach {
//            writeS(it.name)
//            writeD(it.charId)
//            writeS(client.account.account)
//            writeD(_sessionId)
//            writeD(it.clanId)
//            writeD(0x00) // Builder level
//            writeD(it.sex)
//            writeD(it.getRace())
//            writeD(it.getBaseClassId())
//            writeD(0x01) // active ??
//            writeD(it.getX())
//            writeD(it.getY())
//            writeD(it.getZ())
//            writeF(it.getCurrentHp())
//            writeF(it.getCurrentMp())
//            writeD(it.getSp())
//            writeQ(it.getExp())
//            writeD(it.getLevel())
//            writeD(it.getKarma())
//            writeD(it.getPkKills())
//            writeD(it.getPvPKills())
//            writeD(0x00)
//            writeD(0x00)
//            writeD(0x00)
//            writeD(0x00)
//            writeD(0x00)
//            writeD(0x00)
//            writeD(0x00)
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_HAIRALL))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_REAR))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_LEAR))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_NECK))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_HEAD))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_LHAND))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_CHEST))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_LEGS))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_FEET))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_BACK))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_HAIR))
//            writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_FACE))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_HAIRALL))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_REAR))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_LEAR))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_NECK))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_RFINGER))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_LFINGER))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_HEAD))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_RHAND))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_LHAND))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_CHEST))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_LEGS))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_FEET))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_BACK))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_RHAND))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_HAIR))
//            writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_FACE))
//            writeD(it.getHairStyle())
//            writeD(it.getHairColor())
//            writeD(it.getFace())
//            writeF(it.getMaxHp())
//            writeF(it.getMaxMp())
//            writeD(if (it.getAccessLevel() > -1) if (it.getDeleteTimer() > 0) ((it.getDeleteTimer() - System.currentTimeMillis()) / 1000) else 0 else -1)
//            writeD(it.getClassId())
//            writeD(if (i == _activeId) 0x01 else 0x00)
//            writeC(min(127, it.getEnchantEffect()))
//            writeD(it.getAugmentationId())
        }
    }
}