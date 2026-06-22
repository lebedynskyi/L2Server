package lineage.vetal.server.game.game.handler.request.movement

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.movement.usecase.MovementUseCase
import lineage.vetal.server.game.game.handler.request.movement.validation.MovementValidation
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onValid

private const val TAG = "RequestMovementHandler"

class RequestMovementHandler(
    private val context: GameContext,
    private val movementValidation: MovementValidation = MovementValidation(),
    private val movementUseCase: MovementUseCase = MovementUseCase(context)
) {
    fun onRequestMoveTo(player: PlayerObject, destination: Position, intention: Intention? = null) {
        movementValidation.validate(player, destination)
            .onValid {
                movementUseCase.onMovementSuccess(player, destination, intention)
            }.onError {
                movementUseCase.onMovementFail(player, it)
            }
    }

    fun onRequestValidatePosition(player: PlayerObject, position: Position, heading: Int) {
        writeDebug(TAG, "${player.name} Client pos={x=${position.x}, y=${position.y}, z=${position.z}, h=$heading}")
    }
}
