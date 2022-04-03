package lineage.vetal.server.login.db

import lineage.vetal.server.core.DataBaseConfig
import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.login.game.model.template.CharacterTemplate

class GameDatabase(
    dataBaseConfig: DataBaseConfig,
    characterTemplates: MutableMap<Int, CharacterTemplate>
) {
    val accountDao: AccountDao
    val charactersDao: CharactersDao

    init {
        val dbConnection = DBConnection(dataBaseConfig)
        charactersDao = CharactersDao(dbConnection, characterTemplates)
        accountDao = AccountDao(dbConnection)
    }
}