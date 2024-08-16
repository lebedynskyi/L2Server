package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.CharSelectionSlot
import lineage.vetal.server.game.game.model.inventory.PaperDollSlot
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class CharSlotList(
    private val gameClient: GameClient,
    private val slots: List<CharSelectionSlot>,
    private val lastActiveIndex: Int,
) : GameServerPacket() {
    override val opCode: Byte = 0x13

    override fun write() {
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
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.HAIRALL))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.REAR))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.LEAR))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.NECK))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.RFINGER))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.LFINGER))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.HEAD))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.RHAND))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.LHAND))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.GLOVES))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.CHEST))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.LEGS))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.FEET))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.UNDERWEAR))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.RHAND))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.HAIR))
            writeD(it.inventory.getItemObjectIdFrom(PaperDollSlot.FACE))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.HAIRALL))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.REAR))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.LEAR))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.NECK))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.RFINGER))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.LFINGER))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.HEAD))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.RHAND))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.LHAND))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.GLOVES))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.CHEST))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.LEGS))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.FEET))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.UNDERWEAR))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.RHAND))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.HAIR))
            writeD(it.inventory.getItemIdFrom(PaperDollSlot.FACE))
            writeD(it.hairStyle)
            writeD(it.hairColor)
            writeD(it.face)
            writeF(it.maxHp)
            writeF(it.maxMp)
            // TODO move out from here
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