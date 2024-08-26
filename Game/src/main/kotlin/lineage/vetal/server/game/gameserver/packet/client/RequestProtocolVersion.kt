package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestProtocolVersion : GamePacket() {
    var version: Int = -1

    override fun read() {
        version = readD()
    }
}