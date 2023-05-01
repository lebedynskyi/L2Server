package lineage.vetal.server.game.game.manager

import kotlinx.coroutines.*

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.model.player.SayType
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private const val TAG = "AnnounceManager"

class GameAnnounceManager(
    private val chatManager: ChatManager
){
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val announcements = listOf("Hello on mega server", "This is the best server", "I did it", "Another random message")
    private var isRunning = false;

    fun start() {
        isRunning = true
        coroutineScope.launch {
            while (isRunning) {
                delay(TimeUnit.SECONDS.toMillis(Random.nextLong(30, 60)))

                if (isRunning) {
                    val msg = announcements.random()
                    chatManager.announce(msg, SayType.ANNOUNCEMENT)
                    writeInfo(TAG, "-> $msg")
                }
            }
        }
    }
}