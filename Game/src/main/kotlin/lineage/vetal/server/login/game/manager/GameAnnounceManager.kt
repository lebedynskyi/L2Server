package lineage.vetal.server.login.game.manager

import kotlinx.coroutines.*

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.game.GameWorld
import lineage.vetal.server.login.game.model.player.SayType
import lineage.vetal.server.login.gameserver.packet.server.CreatureSay
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private const val TAG = "AnnounceManager"

class GameAnnounceManager(
    private val gameWorld: GameWorld
){

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val announcements = listOf("Hello on mega server", "This is the best server", "I did it", "Another random message")
    private val isRunning = false;

    fun start() {
        coroutineScope.launch {
            while (isRunning) {
                delay(TimeUnit.SECONDS.toMillis(Random.nextLong(30, 60)))

                if (isRunning) {
                    val msg = announcements.random()
                    gameWorld.broadCastPacket(CreatureSay(SayType.ANNOUNCEMENT, msg))
                    writeInfo(TAG, "-> $msg")
                }
            }
        }
    }
}