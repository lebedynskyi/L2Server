package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestUseItem : GamePacket() {
    var objectId: Int = 0
    var ctrlPressed = false

    override fun read() {
        objectId = readD()
        ctrlPressed = readD() != 0
    }
}