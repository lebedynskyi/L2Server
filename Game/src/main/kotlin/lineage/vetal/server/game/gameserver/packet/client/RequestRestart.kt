package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestRestart : GamePacket() {
    override fun execute(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        context.worldManager.onPlayerRestart(client, player)
    }
}