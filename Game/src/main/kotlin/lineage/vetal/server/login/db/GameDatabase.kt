package lineage.vetal.server.login.db

import lineage.vetal.server.core.ConfigDataBase
import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.login.game.model.template.CharTemplate

class GameDatabase(
    configDataBase: ConfigDataBase,
    characterTemplates: MutableMap<Int, CharTemplate>
) {
    val accountDao: AccountDao
    val charactersDao: CharactersDao
    val spawnDao: SpawnDao

    init {
        val dbConnection = DBConnection(configDataBase)
        charactersDao = CharactersDao(dbConnection, characterTemplates)
        accountDao = AccountDao(dbConnection)
        spawnDao = SpawnDao(dbConnection)
    }
}