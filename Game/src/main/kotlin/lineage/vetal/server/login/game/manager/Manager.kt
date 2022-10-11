package lineage.vetal.server.login.game.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class Manager {
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    val isRunning = true

    abstract fun start()
}