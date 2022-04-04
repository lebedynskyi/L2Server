package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.packet.GamePacket
import lineage.vetal.server.login.gameclient.packet.server.UserInfo

class EnterWorld : GamePacket() {
    override fun execute(client: GameClient, context: GameContext) {
        writeInfo("ENTER", "Player ${client.player?.name} in game")
        val player = client.player ?: return

        client.sendPacket(UserInfo(player))
    }

    override fun read() {

    }
}