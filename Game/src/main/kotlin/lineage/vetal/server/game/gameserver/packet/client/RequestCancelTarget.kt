package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GameClientPacket

class RequestCancelTarget : GameClientPacket() {
    var unselect = 0

    override fun read() {
        unselect = readH()
    }
}