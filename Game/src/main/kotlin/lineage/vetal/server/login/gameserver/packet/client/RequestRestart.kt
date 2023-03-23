package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket

class RequestRestart : GamePacket() {
    override fun read() {

    }

    override fun execute(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        context.worldManager.onPlayerRestart(client, player)
    }
}