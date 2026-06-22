package lineage.vetal.server.game.game.manager

import lineage.vetal.server.game.game.handler.request.chat.RequestChatHandler
import lineage.vetal.server.game.game.task.TickTask
import java.time.Clock

private val defaultAnnouncements = listOf(
    "Welcome on Vitalii Lebedynskyi server",
    "This is the best Vetalll server",
    "I did an announce manager!",
    "Some random message"
)

class AnnounceManager(
    private val requestChatHandler: RequestChatHandler,
    private val announcements: List<String> = defaultAnnouncements,
): TickTask() {
    override suspend fun onTick(clock: Clock) {
        val msg = announcements.random()
        requestChatHandler.announce(msg)
    }
}