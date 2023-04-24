package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.Dao
import lineage.vetal.server.core.model.AccountInfo

private const val FIND_ACCOUNT_SQL = "SELECT * FROM `accounts` WHERE login=?"
private const val INSERT_ACCOUNT_SQL = "INSERT INTO `accounts` (id, login, password) VALUES (?,?,?)"

class AccountDao(
    db: DBConnection
) : Dao(db) {
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

    fun insertAccount(account: AccountInfo): Boolean {
        return insertOrUpdate(INSERT_ACCOUNT_SQL) {
            it.setString(1, account.id.toString())
            it.setString(2, account.account)
            it.setString(3, account.password)
        }
    }
}