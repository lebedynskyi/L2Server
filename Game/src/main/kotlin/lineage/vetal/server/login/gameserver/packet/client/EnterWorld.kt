package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.GameClientState
import lineage.vetal.server.login.gameserver.packet.GamePacket
import lineage.vetal.server.login.gameserver.packet.server.UserInfo
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