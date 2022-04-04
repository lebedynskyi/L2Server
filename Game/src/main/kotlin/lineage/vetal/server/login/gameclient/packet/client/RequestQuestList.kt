package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.packet.GamePacket
import lineage.vetal.server.login.gameclient.packet.server.QuestList

class RequestQuestList : GamePacket() {
    override fun execute(client: GameClient, context: GameContext) {
        client.player ?: return
        client.sendPacket(QuestList())
    }

    override fun read() {

    }
}