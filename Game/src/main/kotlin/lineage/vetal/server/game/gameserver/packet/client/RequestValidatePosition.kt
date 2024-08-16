package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestValidatePosition : GamePacket() {
    private var currentX = 0
    private var currentY = 0
    private var currentZ = 0
    private var heading = 0
    private var boatId = 0

    override fun read() {
        currentX = readD()
        currentY = readD()
        currentZ = readD()
        heading = readD()
        boatId = readD()
    }

    override fun executeImpl(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        context.movementManager.onPlayerValidatePosition(player, SpawnPosition(currentX, currentY, currentZ, heading))
    }
}