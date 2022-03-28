package lineage.vetal.server.login.db

import lineage.vetal.server.core.DataBaseConfig
import lineage.vetal.server.core.db.DBConnection

class LoginDatabase(dataBaseConfig: DataBaseConfig) {
    val accountsDao: AccountDao

    init {
        val dbConnection = DBConnection(dataBaseConfig)

        accountsDao = AccountDao(dbConnection)
    }
}