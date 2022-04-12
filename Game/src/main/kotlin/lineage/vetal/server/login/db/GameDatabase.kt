package lineage.vetal.server.login.db

import lineage.vetal.server.core.DataBaseConfig
import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.login.game.model.template.CharTemplate

class GameDatabase(
    dataBaseConfig: DataBaseConfig,
    characterTemplates: MutableMap<Int, CharTemplate>
) {
    val accountDao: AccountDao
    val charactersDao: CharactersDao
    val spawnDao: SpawnDao

    init {
        val dbConnection = DBConnection(dataBaseConfig)
        charactersDao = CharactersDao(dbConnection, characterTemplates)
        accountDao = AccountDao(dbConnection)
        spawnDao = SpawnDao(dbConnection)
    }
}