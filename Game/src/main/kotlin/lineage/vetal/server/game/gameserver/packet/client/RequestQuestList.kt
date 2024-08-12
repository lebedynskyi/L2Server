package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket
import lineage.vetal.server.game.gameserver.packet.server.QuestList

class RequestQuestList : GamePacket() {
    override fun executeImpl(client: GameClient, context: GameContext) {
        client.player ?: return
        client.sendPacket(QuestList())
    }
}