package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.game.model.location.Location
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket

class RequestMoveToLocation : GamePacket() {
    private var targetX = 0
    private var targetY = 0
    private var targetZ = 0
    private var startX = 0
    private var startY = 0
    private var startZ = 0

    override fun read() {
        targetX = readD()
        targetY = readD()
        targetZ = readD()
        startX = readD()
        startY = readD()
        startZ = readD()
    }

    override fun execute(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        val startLocation = Location(startX, startY, startZ)
        val finishLocation = Location(targetX, targetY, targetZ)
        context.movementManager.startMovement(player, startLocation, finishLocation)
    }
}