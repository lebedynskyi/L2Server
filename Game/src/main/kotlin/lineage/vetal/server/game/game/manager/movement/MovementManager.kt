package lineage.vetal.server.game.game.manager.movement

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onSuccess
import lineage.vetal.server.game.gameserver.packet.server.MoveToLocation
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "MovementManager"

class MovementManager(
    private val context: GameContext
) {
    private val movementValidation = MovementValidation()

    fun onPlayerStartMovement(player: PlayerObject, destination: Position) {
        movementValidation.validate(player, destination)
            .onSuccess {
                context.behaviourManager.moveTo(player, destination)
                context.worldManager.broadCast(player.region, MoveToLocation(player, destination))
            }.onError {
                writeDebug(TAG, "Move validation error -> $it")
            }
    }

    fun onPlayerValidatePosition(player: PlayerObject, loc: SpawnPosition) {
        player.clientPosition = loc
    }

    fun onCreatureStartMovement() {

    }

    //?????
    fun onCreatureArrived() {

    }
}