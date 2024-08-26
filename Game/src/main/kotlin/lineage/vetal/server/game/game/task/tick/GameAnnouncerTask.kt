package lineage.vetal.server.game.game.task.tick

import lineage.vetal.server.game.game.handler.request.chat.RequestChatHandler
import java.time.Clock

class GameAnnouncerTask(
    private val requestChatHandler: RequestChatHandler,
    private val announcements: List<String>,
) : TickTask() {
    override suspend fun onTick(clock: Clock) {
        val msg = announcements.random()
        requestChatHandler.announce(msg)
    }
}