package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.Dao
import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.model.AccountInfo

class AccountDao(
    db: DBConnection
) : Dao(db) {
    val FIND_ACCOUNT_SQL = "SELECT * FROM accounts WHERE account = ? AND password = ?"
    val INSERT_ACCOUNT_SQL = "INSERT INTO accounts (account, password) VALUES (?, ?)"

    fun findAccount(account: String, password: String): AccountInfo? {
        val result = query(FIND_ACCOUNT_SQL) {
            it.setString(0, account)
            it.setString(1, password)
        }

        if (result.first()) {
            return AccountInfo(result.getString(0))
        }

        return null
    }

    fun insertAccount(account: String, password: String): Boolean {
        return insertUpdate(INSERT_ACCOUNT_SQL) {
            it.setString(0, account)
            it.setString(1, password)
        } > 0
    }
}