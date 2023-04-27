package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.game.model.item.ItemObject
import lineage.vetal.server.login.gameserver.packet.GameServerPacket

class SpawnItem(item: ItemObject) : GameServerPacket() {
    private val _objectId: Int
    private val _itemId: Int
    private val _x: Int
    private val _y: Int
    private val _z: Int
    private val _stackable: Int
    private val _count: Int

    init {
        _objectId = item.objectId
        _itemId = item.template.id
        _x = item.position.x
        _y = item.position.y
        _z = item.position.z
        _stackable = if (item.template.stackable) 0x01 else 0x00
        _count = item.count
    }

    override fun write() {
        writeC(0x0b)
        writeD(_objectId)
        writeD(_itemId)
        writeD(_x)
        writeD(_y)
        writeD(_z)
        writeD(_stackable)
        writeD(_count)
        writeD(0x00)
    }
}