package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.GameObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class ValidateLocation(
    private val gameObject: GameObject
) : GameServerPacket() {
    override val opCode: Byte = 0x61

    override fun write() {
        writeD(gameObject.objectId)
        writeD(gameObject.position.x)
        writeD(gameObject.position.y)
        writeD(gameObject.position.z)
        writeD(gameObject.position.heading)
    }
}