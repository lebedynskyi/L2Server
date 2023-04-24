package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.Dao
import lineage.vetal.server.core.model.AccountInfo
import java.util.*

class AccountDao(
    db: DBConnection
) : Dao(db) {
    private val FIND_ACCOUNT_SQL = "SELECT * FROM `accounts` WHERE login=?"
    private val INSERT_ACCOUNT_SQL = "INSERT INTO `accounts` (login, password) VALUES (?, ?)"

    fun findAccount(account: String): AccountInfo? {
        return querySingle(FIND_ACCOUNT_SQL,
            onPrepare = { it.setString(1, account) },
            onTransform = {
                AccountInfo(
                    it.getString(1),
                    it.getString(2),
                    it.getString(3)
                )
            }
        )
    }

    fun insertAccount(id: UUID, account: String, password: String): Boolean {
        return insertOrUpdate(INSERT_ACCOUNT_SQL) {
            it.setString(1, id.toString())
            it.setString(2, account)
            it.setString(3, password)
        }
    }
}