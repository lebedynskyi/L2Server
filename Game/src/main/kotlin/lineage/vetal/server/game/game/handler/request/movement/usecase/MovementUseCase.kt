package lineage.vetal.server.game.game.handler.request.movement.usecase

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.movement.validation.MovementValidationValidationError
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position

private const val TAG = "MovementUseCase"

class MovementUseCase(
    private val context: GameContext
) {
    internal fun onMovementSuccess(player: PlayerObject, destination: Position, intention: Intention?) {
        context.movementManager.startMoveToTask(player, destination, intention)
    }

    internal fun onMovementFail(
        player: PlayerObject, reason: MovementValidationValidationError
    ) {
        writeDebug(TAG, "Start move error -> $reason")
    }
}