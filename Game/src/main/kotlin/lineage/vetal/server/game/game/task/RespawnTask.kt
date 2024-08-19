package lineage.vetal.server.game.game.task

import kotlinx.coroutines.CoroutineDispatcher
import lineage.vetal.server.game.game.GameContext

class RespawnTask(
    private val context: GameContext,
    ioDispatcher: CoroutineDispatcher
) : AbstractTask(ioDispatcher) {

    override suspend fun onTick() {

    }
}