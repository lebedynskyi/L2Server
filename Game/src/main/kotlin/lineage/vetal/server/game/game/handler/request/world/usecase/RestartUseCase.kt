package lineage.vetal.server.game.game.handler.request.world.usecase

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.world.validation.RestartValidationError
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.GameClientState
import lineage.vetal.server.game.gameserver.packet.server.RestartResponse

class RestartUseCase(
    private val context: GameContext,
) {
    internal fun onRestartSuccess(player: PlayerObject, client: GameClient) {
        client.clientState = GameClientState.LOBBY
        client.sendPacket(RestartResponse.STATIC_PACKET_OK)
        context.requestAuthHandler.onCharSlotSelection(client)
        context.gameWorld.removePlayerFromWorld(client, player)
    }

    internal fun onRestartFailed(player: PlayerObject, client: GameClient, reason: RestartValidationError) {
        client.sendPacket(RestartResponse.STATIC_PACKET_FAIL)
    }
}