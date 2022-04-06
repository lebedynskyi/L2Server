package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.login.game.GameObject

class DeleteObject(
    val obj: GameObject,
    val isSeated: Boolean = false
) : SendablePacket() {

    override fun write() {
        writeC(0x12)
        writeD(obj.objectId)
        writeD(if (isSeated) 0x00 else 0x01) // 0 - stand up and delete, 1 - delete
    }
}