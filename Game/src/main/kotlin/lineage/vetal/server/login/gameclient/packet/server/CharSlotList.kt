package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.game.model.CharSelectionSlot
import lineage.vetal.server.login.gameclient.packet.GameServerPacket

class CharSlotList(
    private val gameClient: GameClient,
    private val slots: List<CharSelectionSlot>,
    var lastActiveIndex: Int = -1
) : GameServerPacket() {
    override fun write() {
        if (lastActiveIndex <= -1) {
            var lastAccess = 0L
            slots.forEachIndexed { index, slot ->
                if (slot.lastAccess > lastAccess) {
                    lastAccess = slot.lastAccess
                    lastActiveIndex = index
                }
            }
        }

        writeC(0x13)
        writeD(slots.size)

        slots.forEachIndexed { index, it ->
            writeS(it.name)
            writeD(it.charId)
            writeS(gameClient.account.account)
            writeD(gameClient.sessionKey.playOkID1)
            writeD(it.clanId)
            writeD(0x00) // Builder level
            writeD(it.sex.ordinal)
            writeD(it.race)
            writeD(it.baseClassId)
            writeD(0x01) // active ??
            writeD(it.x)
            writeD(it.y)
            writeD(it.z)
            writeF(it.currentHp)
            writeF(it.currentMp)
            writeD(it.sp)
            writeQ(it.exp)
            writeD(it.level)
            writeD(it.karma)
            writeD(it.pkKills)
            writeD(it.pvPKills)
            writeD(0x00)
            writeD(0x00)
            writeD(0x00)
            writeD(0x00)
            writeD(0x00)
            writeD(0x00)
            writeD(0x00)
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_HAIRALL))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_REAR))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_LEAR))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_NECK))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_HEAD))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_LHAND))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_CHEST))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_LEGS))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_FEET))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_BACK))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_HAIR))
            writeD(0)//writeD(it.getPaperdollObjectId(Inventory.PAPERDOLL_FACE))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_HAIRALL))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_REAR))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_LEAR))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_NECK))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_RFINGER))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_LFINGER))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_HEAD))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_RHAND))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_LHAND))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_CHEST))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_LEGS))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_FEET))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_BACK))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_RHAND))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_HAIR))
            writeD(0)//writeD(it.getPaperdollItemId(Inventory.PAPERDOLL_FACE))
            writeD(it.hairStyle)
            writeD(it.hairColor)
            writeD(it.face)
            writeF(it.maxHp)
            writeF(it.maxMp)
            writeD(
                if (it.accessLevel > -1)
                    if (it.deleteTimer > 0)
                        ((it.deleteTimer - System.currentTimeMillis()) / 1000).toInt()
                    else 0
                else -1
            )
            writeD(it.classId)
            writeD(if (index == lastActiveIndex) 0x01 else 0x00)
            writeC(0)//writeC(min(127, it.getEnchantEffect()))
            writeD(0)//writeD(it.getAugmentationId())
        }
    }
}