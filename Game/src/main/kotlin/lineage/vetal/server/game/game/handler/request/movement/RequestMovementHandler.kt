package lineage.vetal.server.game.game.handler.request.movement

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.movement.validation.BehaviourMoveToValidation
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.validation.onError
import lineage.vetal.server.game.game.validation.onSuccess

private const val TAG = "RequestMovementHandler"

class RequestMovementHandler(
    private val context: GameContext,
    private val behaviourMoveToValidation: BehaviourMoveToValidation = BehaviourMoveToValidation(),
) {
    fun onPlayerStartMovement(player: PlayerObject, destination: Position, intention: Intention? = null) {
        behaviourMoveToValidation.validate(player, destination)
            .onSuccess {
                context.movementManager.startMoveToTask(player, destination, intention)
            }.onError {
                writeDebug(TAG, "Start move error -> $it")
            }
    }
}