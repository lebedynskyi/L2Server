package lineage.vetal.server.game.game.task.tick

import lineage.vetal.server.game.game.handler.request.item.RequestItemHandler
import java.time.Clock

private const val TAG = "DeleteItemTask"

class DeleteItemOnGroundTickTask(
    private val requestItemHandler: RequestItemHandler
) : TickTask() {
    override suspend fun onTick(clock: Clock) {

    }
}