package lineage.vetal.server.game.game.handler.tick.spawn

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.GameContext

private const val TAG = "SpawnManager"

class SpawnManager(
    private val context: GameContext
) {
    init {
        val spawnList = context.gameDatabase.spawnDao.getSpawnList()
        for (spawnData in spawnList) {
            val npc = context.objectFactory.createNpcObject(spawnData.npcTemplateId, spawnData.spawnPosition)
            context.gameWorld.onNpcAdded(npc)
        }
        writeInfo(TAG, "Loaded ${spawnList.size} NPC")
    }
}