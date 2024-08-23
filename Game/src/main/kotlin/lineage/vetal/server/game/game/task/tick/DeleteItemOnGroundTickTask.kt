package lineage.vetal.server.game.game.task.tick

import lineage.vetal.server.game.game.manager.item.ItemManager
import java.time.Clock

private const val TAG = "DeleteItemTask"

class DeleteItemOnGroundTickTask(
    private val itemManager: ItemManager
) : TickTask() {
    override suspend fun onTick(clock: Clock) {

    }
}