package lineage.vetal.server.login.gameclient.packet

import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.login.game.model.location.Location

abstract class GameServerPacket : SendablePacket() {
    fun writeLoc(location: Location) {
        writeD(location.x)
        writeD(location.y)
        writeD(location.z)
    }
}