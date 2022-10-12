package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket

class RequestMoveToLocation : GamePacket() {
    private var targetX = 0
    private var targetY = 0
    private var targetZ = 0
    private var originX = 0
    private var originY = 0
    private var originZ = 0

    override fun execute(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        val newLocation = SpawnLocation(targetX, targetY, targetZ, 0)
        player.moveToLocation(newLocation)
    }

    override fun read() {
        targetX = readD()
        targetY = readD()
        targetZ = readD()
        originX = readD()
        originY = readD()
        originZ = readD()
    }
}