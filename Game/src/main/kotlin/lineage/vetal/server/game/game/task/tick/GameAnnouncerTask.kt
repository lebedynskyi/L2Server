package lineage.vetal.server.game.game.task.tick

import lineage.vetal.server.game.game.manager.ChatManager
import java.time.Clock

class GameAnnouncerTask(
    private val chatManager: ChatManager,
    private val announcements: List<String>,
) : TickTask() {
    override suspend fun onTick(clock: Clock) {
        val msg = announcements.random()
        chatManager.announce(msg)
    }
}