package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.GameObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class DeleteObject(
    private val obj: GameObject,
    private val isSeated: Boolean = false
) : GameServerPacket() {
    override val opCode: Byte = 0x12

    override fun write() {
        writeD(obj.objectId)
        writeD(if (isSeated) 0x00 else 0x01) // 0 - stand up and delete, 1 - delete
    }
}