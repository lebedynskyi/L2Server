package lineage.vetal.server.login.db

import lineage.vetal.server.core.DataBaseConfig
import lineage.vetal.server.core.db.DBConnection

class GameDatabase(dataBaseConfig: DataBaseConfig) {
    val accountDao: AccountDao
    val charactersDao: CharactersDao

    init {
        val dbConnection = DBConnection(dataBaseConfig)
        charactersDao = CharactersDao(dbConnection)
        accountDao = AccountDao(dbConnection)
    }
}