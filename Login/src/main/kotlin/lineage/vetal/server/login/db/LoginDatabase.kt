package lineage.vetal.server.login.db

import lineage.vetal.server.core.ConfigDataBase
import lineage.vetal.server.core.db.DBConnection

class LoginDatabase(configDataBase: ConfigDataBase) {
    val accountsDao: AccountDao

    init {
        val dbConnection = DBConnection(configDataBase)

        accountsDao = AccountDao(dbConnection)
    }
}