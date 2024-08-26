package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestCancelTarget : GamePacket() {
    private var unselect = 0

    override fun read() {
        unselect = readH()
    }

    override fun executeImpl(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        context.actionManager.onPlayerCancelAction(player, unselect)
    }
}