package lineage.vetal.server.game.gameserver.packet

import vetalll.server.sock.ReadablePacket

abstract class GamePacket : ReadablePacket() {
    override fun read() {}
}