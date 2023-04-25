package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.game.model.item.ItemObject
import lineage.vetal.server.login.gameserver.packet.GameServerPacket

class DropItem(
    private val item: ItemObject,
    private val throwerId: Int
): GameServerPacket() {
    override fun write() {
        writeC(0x0c)
        writeD(throwerId)
        writeD(item.objectId)
        writeD(item.template.id)

        writeD(item.position.x)
        writeD(item.position.y)
        writeD(item.position.z)

        // only show item count if it is a stackable item
        if (item.template.stackable)
            writeD(0x01);
        else
            writeD(0x00);
        writeD(item.count);

        writeD(1); // unknown
    }
}