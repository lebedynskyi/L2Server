package lineage.vetal.server.game.gameserver.packet

import lineage.vetal.server.game.game.model.position.Position
import vetalll.server.sock.WriteablePacket

abstract class GameServerPacket : WriteablePacket() {
    fun writeLoc(location: Position) {
        writeD(location.x)
        writeD(location.y)
        writeD(location.z)
    }
}