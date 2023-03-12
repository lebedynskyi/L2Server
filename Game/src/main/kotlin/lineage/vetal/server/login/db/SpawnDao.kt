package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.Dao
import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.game.model.npc.SpawnData

class SpawnDao(
    dbConnection: DBConnection
) : Dao(dbConnection) {
    private val QUERY_SPAWN_LIST = "SELECT * from spawnlist"

    fun getSpawnList(): List<SpawnData> {
        return queryList(QUERY_SPAWN_LIST) {
            SpawnData(
                it.getInt("npc_templateid"),
                SpawnLocation(it.getInt("locx"), it.getInt("locy"), it.getInt("locz"), it.getInt("heading")),
                it.getInt("respawn_delay"),
                it.getInt("respawn_rand"),
                it.getInt("periodOfDay")
            )
        }
    }
}