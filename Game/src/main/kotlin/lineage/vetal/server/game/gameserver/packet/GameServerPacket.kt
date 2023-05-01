package lineage.vetal.server.game.gameserver.packet

import lineage.vetal.server.game.game.model.position.Position
import vetal.server.network.SendablePacket

abstract class GameServerPacket : SendablePacket() {
    fun writeLoc(location: Position) {
        writeD(location.x)
        writeD(location.y)
        writeD(location.z)
    }
}