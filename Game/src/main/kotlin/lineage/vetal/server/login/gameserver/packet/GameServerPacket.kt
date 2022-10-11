package lineage.vetal.server.login.gameserver.packet

import lineage.vetal.server.login.game.model.location.Location
import vetal.server.network.SendablePacket

abstract class GameServerPacket : SendablePacket() {
    fun writeLoc(location: Location) {
        writeD(location.x)
        writeD(location.y)
        writeD(location.z)
    }
}