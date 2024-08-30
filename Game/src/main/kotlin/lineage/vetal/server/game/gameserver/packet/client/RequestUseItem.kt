package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GameClientPacket

class RequestUseItem : GameClientPacket() {
    var objectId: Int = 0
    var ctrlPressed = false

    override fun read() {
        objectId = readD()
        ctrlPressed = readD() != 0
    }
}