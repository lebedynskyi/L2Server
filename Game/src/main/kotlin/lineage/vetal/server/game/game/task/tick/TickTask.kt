package lineage.vetal.server.game.game.task.tick

import java.time.Clock
import java.util.UUID

open class TickTask {
    val id = UUID.randomUUID()

    open suspend fun onStart(clock: Clock) {}
    open suspend fun onTick(clock: Clock) {}
    open suspend fun onStop(clock: Clock) {}
}