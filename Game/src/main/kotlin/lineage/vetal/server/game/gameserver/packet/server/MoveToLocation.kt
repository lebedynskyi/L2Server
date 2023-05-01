package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class MoveToLocation(
    creature: CreatureObject,
    destination: Position
) : GameServerPacket() {
    private val _objectId: Int

    private var targetX = 0
    private var targetY = 0
    private var targetZ = 0
    private var originX = 0
    private var originY = 0
    private var originZ = 0

    init {
        _objectId = creature.objectId
        originX = creature.position.x
        originY = creature.position.y
        originZ = creature.position.z
        targetX = destination.x
        targetY = destination.y
        targetZ = destination.z
    }

    override fun write() {
        writeC(0x01)
        writeD(_objectId)

        writeD(targetX)
        writeD(targetY)
        writeD(targetZ)

        writeD(originX)
        writeD(originY)
        writeD(originZ)
    }
}