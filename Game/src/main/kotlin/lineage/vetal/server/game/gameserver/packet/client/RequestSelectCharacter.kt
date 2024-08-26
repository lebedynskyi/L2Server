package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestSelectCharacter : GamePacket() {
    var slotIndex = -1

    override fun read() {
        slotIndex = readD()
        readH() // Not used.
        readD() // Not used.
        readD() // Not used.
        readD() // Not used.
    }
}