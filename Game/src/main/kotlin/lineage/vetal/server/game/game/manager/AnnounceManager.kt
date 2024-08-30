package lineage.vetal.server.game.game.manager

import lineage.vetal.server.game.game.handler.request.chat.RequestChatHandler
import lineage.vetal.server.game.game.task.TickTask
import java.time.Clock

class AnnounceManager(
    private val requestChatHandler: RequestChatHandler,
    private val announcements: List<String>,
): TickTask() {
    override suspend fun onTick(clock: Clock) {
        val msg = announcements.random()
        requestChatHandler.announce(msg)
    }
}