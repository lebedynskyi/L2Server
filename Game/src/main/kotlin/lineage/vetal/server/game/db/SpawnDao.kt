package lineage.vetal.server.game.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.Dao
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.npc.NpcSpawnData

class SpawnDao(
    dbConnection: DBConnection
) : Dao(dbConnection) {
    private val QUERY_SPAWN_LIST = "SELECT * from spawnlist"

    fun getSpawnList(): List<NpcSpawnData> {
        return queryList(QUERY_SPAWN_LIST) {
            NpcSpawnData(
                it.getInt("npc_templateid"),
                SpawnPosition(it.getInt("locx"), it.getInt("locy"), it.getInt("locz"), it.getInt("heading")),
                it.getInt("respawn_delay"),
                it.getInt("respawn_rand"),
                it.getInt("periodOfDay")
            )
        }
    }
}