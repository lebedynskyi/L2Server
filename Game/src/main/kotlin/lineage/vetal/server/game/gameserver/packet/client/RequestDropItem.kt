package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestDropItem : GamePacket() {
    var objectId = 0
    var count = 0
    var x = 0
    var y = 0
    var z = 0

    override fun read() {
        objectId = readD()
        count = readD()
        x = readD()
        y = readD()
        z = readD()
    }
}