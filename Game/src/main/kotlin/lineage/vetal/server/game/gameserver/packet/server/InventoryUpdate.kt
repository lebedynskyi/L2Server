package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.item.EquipmentObject
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.item.WeaponObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class InventoryUpdate : GameServerPacket() {
    override val opCode: Byte = 0x27

    private val items: MutableList<UpdateInfo> = mutableListOf()

    override fun write() {
        writeH(items.size)

        items.forEach {
            writeH(it.changeType.ordinal)
            writeH(it.type1)
            writeD(it.objectId)
            writeD(it.itemId)
            writeD(it.count)
            writeH(it.type2)
            writeH(it.customType1)
            writeH(if (it.equipped) 0x01 else 0x00)
            writeD(it.bodySlot.gameId)
            writeH(it.enchant)
            writeH(it.customType2)
            writeD(it.augmentationId)
            writeD(it.durationLeft)
        }
    }

    fun onItemRemoved(itemObject: ItemObject) {
        items.add(UpdateInfo(itemObject, ChangeType.REMOVED))
    }

    fun onNewItem(itemObject: ItemObject) {
        items.add(UpdateInfo(itemObject, ChangeType.ADDED))
    }

    fun onModified(itemObject: ItemObject) {
        items.add(UpdateInfo(itemObject, ChangeType.MODIFIED))
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