package lineage.vetal.server.game.game.task.schedule

import kotlinx.coroutines.CoroutineDispatcher
import java.time.Clock

class ScheduleTask(
    private val clock: Clock,
    private val dispatcher: CoroutineDispatcher
) {
    fun onExecute(clock: Clock) {

    }
}