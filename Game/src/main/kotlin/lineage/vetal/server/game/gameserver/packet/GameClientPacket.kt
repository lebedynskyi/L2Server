package lineage.vetal.server.game.gameserver.packet

import vetalll.server.sock.ReadablePacket

abstract class GameClientPacket : ReadablePacket() {
    override fun read() {}
}