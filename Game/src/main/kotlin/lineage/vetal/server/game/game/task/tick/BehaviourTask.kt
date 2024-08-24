package lineage.vetal.server.game.game.task.tick

import lineage.vetal.server.game.game.manager.behaviour.BehaviourManager
import java.time.Clock

class BehaviourTask(
    private val behaviourManager: BehaviourManager,
) : TickTask() {
    override suspend fun onTick(clock: Clock) {
        behaviourManager.onTick()
    }
}