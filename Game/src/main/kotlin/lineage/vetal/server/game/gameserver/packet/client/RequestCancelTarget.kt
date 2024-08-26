package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestCancelTarget : GamePacket() {
    var unselect = 0

    override fun read() {
        unselect = readH()
    }
}