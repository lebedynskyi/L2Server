package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.game.model.item.EquipmentObject
import lineage.vetal.server.login.game.model.item.ItemObject
import lineage.vetal.server.login.game.model.item.WeaponObject
import lineage.vetal.server.login.game.model.template.items.ItemTemplate
import lineage.vetal.server.login.gameserver.packet.GameServerPacket

class InventoryList(
    private val items: Set<ItemObject>,
    private val showWindow: Boolean = false
) : GameServerPacket() {
    override fun write() {
        writeC(0x1b)
        writeH(if (showWindow) 0x01 else 0x00)
        writeH(items.size)

        for (item in items) {
            val template: ItemTemplate = item.template
            writeH(template.type1)
            writeD(item.objectId)
            writeD(item.template.id)
            writeD(item.count)
            writeH(template.type2)
            writeH(item.customType1)
            writeH(if (item is EquipmentObject && item.isEquipped) 0x01 else 0x00)
            writeD(template.bodySlot)
            writeH(item.enchantLevel)
            writeH(item.customType2)
            if (item is WeaponObject && item.isAugmented) {
                writeD(item.augmentationId)
            } else {
                writeD(0x00)
            }
            writeD(item.durationLeft)
        }
    }
}