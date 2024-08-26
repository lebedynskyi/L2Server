package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestValidatePosition : GamePacket() {
    var currentX = 0
    var currentY = 0
    var currentZ = 0
    var heading = 0
    var boatId = 0

    override fun read() {
        currentX = readD()
        currentY = readD()
        currentZ = readD()
        heading = readD()
        boatId = readD()
    }
}