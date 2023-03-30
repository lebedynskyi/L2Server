package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.login.game.model.template.pc.CharTemplate

class GameDatabase(
    characterTemplates: Map<Int, CharTemplate>,
    dbConnection: DBConnection
) {
    val accountDao: AccountDao
    val charactersDao: CharactersDao
    val spawnDao: SpawnDao

    init {
        charactersDao = CharactersDao(dbConnection, characterTemplates)
        accountDao = AccountDao(dbConnection)
        spawnDao = SpawnDao(dbConnection)
    }
}