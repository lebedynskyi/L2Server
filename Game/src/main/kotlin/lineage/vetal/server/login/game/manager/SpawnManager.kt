package lineage.vetal.server.login.game.manager

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.db.GameDatabase
import lineage.vetal.server.login.game.GameObjectFactory

private const val TAG = "SpawnManager"

class SpawnManager(
    private val worldManager: WorldManager,
    private val gameDatabase: GameDatabase,
    private val gameObjectFactory: GameObjectFactory
) {
    init {
        // TODO here we can start some timers for respawn
        val spawnList = gameDatabase.spawnDao.getSpawnList()
        for (spawnData in spawnList) {
            val npc = gameObjectFactory.createNpcObject(spawnData.npcTemplateId, spawnData.spawnPosition)
            worldManager.onNpcAdded(npc)
        }
        writeInfo(TAG, "Loaded ${spawnList.size} NPC" )
    }
}