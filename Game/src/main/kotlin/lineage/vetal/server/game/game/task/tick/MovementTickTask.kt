package lineage.vetal.server.game.game.task.tick

import lineage.vetal.server.game.game.manager.movement.MovementManager
import java.time.Clock

class MovementTickTask(
    private val movementManager: MovementManager,
) : TickTask() {
    override suspend fun onTick(clock: Clock) {
        movementManager.updatePositions()
    }
}