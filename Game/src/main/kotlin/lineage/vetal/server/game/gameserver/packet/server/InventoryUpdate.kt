package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.item.EtcItemObject
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.item.WeaponObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class InventoryUpdate : GameServerPacket() {
    private val items: MutableList<UpdateInfo> = mutableListOf()

    fun onItemRemoved(itemObject: ItemObject) {
        items.add(UpdateInfo(itemObject, ChangeType.REMOVED))
    }

    fun onNewItem(itemObject: ItemObject) {
        items.add(UpdateInfo(itemObject, ChangeType.ADDED))
    }

    fun onModified(itemObject: ItemObject) {
        items.add(UpdateInfo(itemObject, ChangeType.MODIFIED))
    }

    override fun write() {
        writeC(0x27)
        writeH(items.size)

        for (temp in items) {
            writeH(temp.changeType.ordinal)
            writeH(temp.type1)
            writeD(temp.objectId)
            writeD(temp.itemId)
            writeD(temp.count)
            writeH(temp.type2)
            writeH(temp.customType1)
            writeH(if (temp.equipped) 0x01 else 0x00)
            writeD(temp.bodySlot)
            writeH(temp.enchant)
            writeH(temp.customType2)
            writeD(temp.augmentationId)
            writeD(temp.durationLeft)
        }
    }
}

private enum class ChangeType {
    UNCHANGED, ADDED, MODIFIED, REMOVED
}

private data class UpdateInfo(
    val itemObject: ItemObject,
    val changeType: ChangeType
) {
    val type1 = itemObject.template.type1
    val objectId = itemObject.objectId
    val itemId = itemObject.template.id
    val count = itemObject.count
    val type2 = itemObject.template.type2
    val customType1 = itemObject.customType1
    val equipped = itemObject is EquipmentObject && itemObject.isEquipped
    val bodySlot = itemObject.template.bodySlot
    val enchant = itemObject.enchantLevel
    val customType2 = itemObject.customType2
    val augmentationId = if (itemObject is WeaponObject) itemObject.augmentationId else 0
    val durationLeft = itemObject.durationLeft
}