package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestMoveToLocation : GamePacket() {
    var targetX = 0
    var targetY = 0
    var targetZ = 0
    var startX = 0
    var startY = 0
    var startZ = 0

    override fun read() {
        targetX = readD()
        targetY = readD()
        targetZ = readD()
        startX = readD()
        startY = readD()
        startZ = readD()
    }
}