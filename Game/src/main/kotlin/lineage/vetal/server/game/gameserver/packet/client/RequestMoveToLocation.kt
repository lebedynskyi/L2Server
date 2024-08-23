package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket

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

    override fun executeImpl(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        val startLocation = Position(startX, startY, startZ)
        val finishLocation = Position(targetX, targetY, targetZ)
        context.movementManager.onPlayerStartMovement(player, startLocation, finishLocation)
    }
}