package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket
import lineage.vetal.server.login.gameserver.packet.server.LeaveWorld

class RequestQuit: GamePacket() {
    override fun execute(client: GameClient, context: GameContext) {
        client.saveAndClose(LeaveWorld())
        client.player?.let {
            context.gameWorld.decay(it)
        }
    }

    override fun read() {

    }
}