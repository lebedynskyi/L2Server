package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GameClientPacket

class RequestProtocolVersion : GameClientPacket() {
    var version: Int = -1

    override fun read() {
        version = readD()
    }
}