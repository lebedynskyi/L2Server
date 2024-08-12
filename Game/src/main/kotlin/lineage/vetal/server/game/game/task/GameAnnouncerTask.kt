package lineage.vetal.server.game.game.task

import kotlinx.coroutines.*
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.player.SayType
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private const val TAG = "AnnounceManager"

private val announcements = listOf(
    "Welcome on Vitalii Lebedynskyi server",
    "This is the best Vetalll server",
    "I did an announce manager!",
    "Some random message"
)

class GameAnnouncerTask(
    private val context: GameContext,
    dispatcher: CoroutineDispatcher,
) : AbstractTask(dispatcher, period = {
    TimeUnit.SECONDS.toMillis(Random.nextLong(60, 120))
}) {
    init {
        writeInfo(TAG, "Started with ${announcements.size} messages")
    }

    override suspend fun onStart() {
        // Initial delay. Nno need to launch at start of task
        delay(5000)
    }

    override suspend fun onTick() {
        val msg = announcements.random()
        context.chatManager.announce(msg, SayType.ANNOUNCEMENT)

        writeDebug(TAG, "-> $msg")
    }
}