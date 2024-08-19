package lineage.vetal.server.game.db.sql

class AccountSQL {
    companion object {
        const val FIND_ACCOUNT_SQL = "SELECT * FROM `accounts` WHERE login=?"
    }
}
