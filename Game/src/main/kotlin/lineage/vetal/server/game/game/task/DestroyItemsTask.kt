package lineage.vetal.server.game.game.task

import kotlinx.coroutines.CoroutineDispatcher
import lineage.vetal.server.game.game.GameContext

class DestroyItemsTask(
    private val context: GameContext,
    dispatcher: CoroutineDispatcher,
) : AbstractTask(dispatcher, period = { 5000 }) {
    override suspend fun onTick() {
        super.onTick()
    }
}