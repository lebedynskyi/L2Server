package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.GameClientState
import lineage.vetal.server.login.gameclient.packet.GamePacket
import lineage.vetal.server.login.gameclient.packet.server.UserInfo
import java.util.Calendar

class EnterWorld : GamePacket() {
    override fun execute(client: GameClient, context: GameContext) {
        val player = client.player ?: return

        client.clientState = GameClientState.WORLD
        player.client = client
        player.sendPacket(UserInfo(player))
        context.gameDatabase.charactersDao.updateLastAccess(player.id, Calendar.getInstance().timeInMillis)
        context.gameWorld.spawn(player)
    }

    override fun read() {

    }
}