package lineage.vetal.server.core.db

import java.sql.PreparedStatement
import java.sql.ResultSet

abstract class Dao(
    private val dataBase: DBConnection
) {
    fun query(sql: String, block: (PreparedStatement) -> Unit): ResultSet {
        return dataBase.getConnection().let {
            it.prepareStatement(sql)
                .apply(block)
                .executeQuery()
        }
    }

    fun insertUpdate(sql: String, block: (PreparedStatement) -> Unit): Int {
        return dataBase.getConnection().use {
            it.prepareStatement(sql)
                .apply(block)
                .executeUpdate()
        }
    }
}