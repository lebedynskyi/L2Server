package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class SpawnItem(
    private val item: ItemObject
) : GameServerPacket() {
    override val opCode: Byte = 0x0b

    override fun write() {
        writeD(item.objectId)
        writeD(item.template.id)
        writeD(item.position.x)
        writeD(item.position.y)
        writeD(item.position.z)
        writeD(if (item.template.stackable) 0x01 else 0x00)
        writeD(item.count)
        writeD(0x00)
    }
}