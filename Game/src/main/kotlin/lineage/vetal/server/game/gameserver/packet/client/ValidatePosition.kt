package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket

class ValidatePosition : GamePacket() {
    private var currentX = 0
    private var currentY = 0
    private var currentZ = 0

    override fun read() {
        currentX = readD()
        currentY = readD()
        currentZ = readD()
        var unknown = readD()
    }

    override fun execute(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        context.movementManager.onPlayerValidatePosition(player, Position(currentX, currentY, currentZ))
    }
}