package lineage.vetal.server.game.game.manager.spawn

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.task.TickTask
import java.time.Clock

private const val TAG = "SpawnManager"

class SpawnManager(
    private val context: GameContext
) : TickTask() {
    override suspend fun onStart(clock: Clock) {
        super.onStart(clock)
        val spawnList = context.gameDatabase.spawnDao.getSpawnList()
        for (spawnData in spawnList) {
            val npc = context.objectFactory.createNpcObject(spawnData.npcTemplateId, spawnData.spawnPosition)
            context.gameWorld.onNpcAdded(npc)
        }
        writeInfo(TAG, "Loaded ${spawnList.size} NPC")
    }
}
