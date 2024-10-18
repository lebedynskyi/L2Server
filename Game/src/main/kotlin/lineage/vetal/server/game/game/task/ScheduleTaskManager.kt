package lineage.vetal.server.game.game.task

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lineage.vetal.server.core.utils.logs.writeInfo
import java.time.Clock

private const val TAG = "ScheduleTaskManager"

class ScheduleTaskManager(
    private val clock: Clock,
    dispatcher: CoroutineDispatcher
) {
    private val scope = CoroutineScope(dispatcher)

    fun schedule(task: ScheduleTask, delay: Long = 0) {
        writeInfo(TAG, "Scheduled a task '${task::class.java.name}' with delay by $delay")

        scope.launch {
            task.execute(clock)
        }
    }
}
