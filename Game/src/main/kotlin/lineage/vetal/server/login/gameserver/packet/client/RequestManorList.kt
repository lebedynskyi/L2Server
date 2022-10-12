package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket
import lineage.vetal.server.login.gameserver.packet.server.ManorList

class RequestManorList : GamePacket() {
    override fun execute(client: GameClient, context: GameContext) {
        client.player ?: return
        client.sendPacket(ManorList.STATIC_PACKET)
    }

    override fun read() {

    }
}