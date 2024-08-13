package lineage.vetal.server.game.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.DBDao
import lineage.vetal.server.core.model.AccountInfo

private const val FIND_ACCOUNT_SQL = "SELECT * FROM `accounts` WHERE login=?"

class AccountDao(
    db: DBConnection
) : DBDao(db) {
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
}