package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.game.model.location.Location
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket

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
        context.movementManager.onPlayerValidatePosition(player, Location(currentX, currentY, currentZ))
    }
}