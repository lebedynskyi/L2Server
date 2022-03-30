package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.Dao
import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.model.AccountInfo

class AccountDao(
    db: DBConnection
) : Dao(db) {
    private val FIND_ACCOUNT_SQL = "SELECT * FROM `accounts` WHERE login=?"
    private val INSERT_ACCOUNT_SQL = "INSERT INTO `accounts` (login, password) VALUES (?, ?)"

    fun findAccount(account: String): AccountInfo? {
        val result = query(FIND_ACCOUNT_SQL) {
            it.setString(1, account)
        }

        result.use {
            if (result.first()) {
                return AccountInfo(result.getString(1), result.getString(2))
            }
        }

        return null
    }

    fun insertAccount(account: String, password: String): Boolean {
        return insertUpdate(INSERT_ACCOUNT_SQL) {
            it.setString(1, account)
            it.setString(2, password)
        } > 0
    }
}