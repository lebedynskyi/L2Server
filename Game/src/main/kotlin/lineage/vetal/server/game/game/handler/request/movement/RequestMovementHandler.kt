package lineage.vetal.server.game.game.handler.request.movement

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.movement.usecase.MovementUseCase
import lineage.vetal.server.game.game.handler.request.movement.validation.MovementValidation
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.validation.onError
import lineage.vetal.server.game.game.validation.onSuccess

private const val TAG = "RequestMovementHandler"

class RequestMovementHandler(
    private val context: GameContext,
    private val movementValidation: MovementValidation = MovementValidation(),

    private val movementUseCase: MovementUseCase = MovementUseCase(context)
) {
    fun onPlayerStartMovement(player: PlayerObject, destination: Position, intention: Intention? = null) {
        movementValidation.validate(player, destination)
            .onSuccess {
                movementUseCase.onMovementSuccess(player, destination, intention)
            }.onError {
                movementUseCase.onMovementFail(player, it)
            }
    }
}