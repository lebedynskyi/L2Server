package lineage.vetal.server.game.game.task

import java.time.Clock
import java.util.UUID

open class ScheduleTask {
    val id = UUID.randomUUID()

    open suspend fun execute(clock: Clock) {}
}