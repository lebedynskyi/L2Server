package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket
import lineage.vetal.server.game.gameserver.packet.server.ActionFailed

class RequestDropItem : GamePacket() {
    private var objectId = 0
    private var count = 0
    private var x = 0
    private var y = 0
    private var z = 0

    override fun executeImpl(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        context.itemManager.onPlayerDropItem(player, objectId, count, x, y, z)
    }

    override fun read() {
        objectId = readD()
        count = readD()
        x = readD()
        y = readD()
        z = readD()
    }
}