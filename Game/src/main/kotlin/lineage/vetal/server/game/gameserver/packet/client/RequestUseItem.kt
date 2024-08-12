package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestUseItem : GamePacket() {
    var objectId: Int = 0
    var ctrlPressed = false

    override fun executeImpl(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        context.itemManager.onUseItem(client, player, objectId, ctrlPressed)
    }

    override fun read() {
        objectId = readD()
        ctrlPressed = readD() != 0
    }
}