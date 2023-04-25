package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.game.model.item.ItemObject
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
            val template: ItemTemplate = item.itemTemplate
            writeH(template.getType1())
            writeD(item.getObjectId())
            writeD(item.getItemId())
            writeD(item.getCount())
            writeH(template.getType2())
            writeH(item.getCustomType1())
            writeH(if (item.isEquipped()) 0x01 else 0x00)
            writeD(template.getBodyPart())
            writeH(item.getEnchantLevel())
            writeH(item.getCustomType2())
            writeD(if (item.isAugmented()) item.getAugmentation().getId() else 0x00)
            writeD(item.getMana())
        }
    }
}