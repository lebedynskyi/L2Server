package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class DropItem(
    private val item: ItemObject,
    private val throwerId: Int
): GameServerPacket() {
    override val opCode: Byte = 0x0c

    override fun write() {
        writeD(throwerId)
        writeD(item.objectId)
        writeD(item.template.id)

        writeD(item.position.x)
        writeD(item.position.y)
        writeD(item.position.z)

        // only show item count if it is a stackable item
        if (item.template.stackable)
            writeD(0x01)
        else
            writeD(0x00)

        writeD(item.count)

        writeD(1); // unknown
    }
}