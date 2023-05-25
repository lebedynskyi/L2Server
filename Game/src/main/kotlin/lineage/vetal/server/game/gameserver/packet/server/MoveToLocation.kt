package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class MoveToLocation(
    private val creature: CreatureObject,
    private val destination: Position
) : GameServerPacket() {
    override val opCode: Byte = 0x01

    override fun write() {
        writeD(creature.objectId)
        writeD(destination.x)
        writeD(destination.y)
        writeD(destination.z)
        writeD(creature.position.x)
        writeD(creature.position.y)
        writeD(creature.position.z)
    }
}