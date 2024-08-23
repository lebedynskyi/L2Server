package lineage.vetal.server.game.game.task

import kotlinx.coroutines.*
import lineage.vetal.server.game.game.task.schedule.ScheduleTask
import java.time.Clock

class ScheduleTaskManager(
    private val clock: Clock,
    dispatcher: CoroutineDispatcher,
) {
    private val scope = CoroutineScope(dispatcher + Job())

    fun schedule(task: ScheduleTask, time: Long) {
        scope.launch {
            delay(time - clock.millis())
            task.onExecute(clock)
        }
    }
}