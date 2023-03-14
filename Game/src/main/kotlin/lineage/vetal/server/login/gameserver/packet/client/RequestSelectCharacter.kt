package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket

class RequestSelectCharacter : GamePacket() {
    private var slotIndex = -1

    override fun read() {
        slotIndex = readD()
        readH() // Not used.
        readD() // Not used.
        readD() // Not used.
        readD() // Not used.
    }

    override fun execute(client: GameClient, context: GameContext) {
        context.gameLobby.requestSelectChar(client, slotIndex)
    }
}