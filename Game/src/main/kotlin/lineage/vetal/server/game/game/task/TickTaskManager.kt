package lineage.vetal.server.game.game.task

import kotlinx.coroutines.*
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.task.tick.TickTask
import java.time.Clock
import java.util.UUID

private const val TAG = "TickTaskManager"

class TickTaskManager(
    private val clock: Clock,
    dispatcher: CoroutineDispatcher
) {
    private val scope = CoroutineScope(dispatcher + Job())
    private val tasks = mutableMapOf<UUID, Job>()

    fun register(task: TickTask, period: Long, delay: Long = 0) {
        writeInfo(TAG, "Registered a task '${task::class.java.name}' with period $period and delayed by $delay")

        val job = scope.launch {
            delay(delay)
            task.onStart(clock)

            while (isActive) {
                // TODO calculate how many time was consumed by tick. Reduce next delay ?
                task.onTick(clock)
                delay(period)
            }

            // TODO might not be called if coroutine canceled.
            task.onStop(clock)
        }

        tasks[task.id] = job
    }
}