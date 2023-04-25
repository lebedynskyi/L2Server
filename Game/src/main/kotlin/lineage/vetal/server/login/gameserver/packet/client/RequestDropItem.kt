package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket

class RequestDropItem : GamePacket() {
    private var objectId = 0
    private var count = 0
    private var x = 0
    private var y = 0
    private var z = 0

    override fun execute(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        context.itemManager.onPlayerDropItem(client, player, objectId, count, x, y, z)
    }

    override fun read() {
        objectId = readD()
        count = readD()
        x = readD()
        y = readD()
        z = readD()
    }
}