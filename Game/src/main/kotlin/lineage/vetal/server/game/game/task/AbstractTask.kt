package lineage.vetal.server.game.game.task

import kotlinx.coroutines.*

private const val DEFAULT_TASK_PERIOD = 1000L // 1 second

abstract class AbstractTask(
    coroutineDispatcher: CoroutineDispatcher,
    private val period: () -> Long = { DEFAULT_TASK_PERIOD }
) {
    private val coroutineScope = CoroutineScope(coroutineDispatcher + Job())
    private var taskJob: Job? = null

    init {
        taskJob = coroutineScope.launch {
            onStart()
            while (taskJob?.isActive == true) {
                onTick()
                delay(period())
            }
            onStop()
        }
    }

    protected open suspend fun onStart() {}
    protected open suspend fun onTick() {}
    protected open suspend fun onStop() {}
}
