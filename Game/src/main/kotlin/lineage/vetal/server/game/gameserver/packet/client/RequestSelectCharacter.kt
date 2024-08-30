package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GameClientPacket

class RequestSelectCharacter : GameClientPacket() {
    var slotIndex = -1

    override fun read() {
        slotIndex = readD()
        readH() // Not used.
        readD() // Not used.
        readD() // Not used.
        readD() // Not used.
    }
}