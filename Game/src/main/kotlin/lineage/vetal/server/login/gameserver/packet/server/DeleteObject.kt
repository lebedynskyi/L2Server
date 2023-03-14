package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.game.model.GameObject
import lineage.vetal.server.login.gameserver.packet.GameServerPacket

class DeleteObject(
    private val obj: GameObject,
    private val isSeated: Boolean = false
) : GameServerPacket() {

    override fun write() {
        writeC(0x12)
        writeD(obj.objectId)
        writeD(if (isSeated) 0x00 else 0x01) // 0 - stand up and delete, 1 - delete
    }
}