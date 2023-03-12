package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.DBConnection

class LoginDatabase(
    dbConnection: DBConnection
) {
    val accountsDao = AccountDao(dbConnection)
}