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

    fun insertOrUpdate(sql: String, block: (PreparedStatement) -> Unit = {}): Boolean {
        return dataBase.getConnection().use {
            it.prepareStatement(sql)
                .apply(block)
                .executeUpdate()
        } > 0
    }

    fun <T> ResultSet.firstOrNull(transform: (ResultSet) -> T): T? {
        if (first()) {
            use {
                return transform.invoke(it)
            }
        } else {
            return null
        }
    }

    fun <T> ResultSet.listOrEmpty(transform: (ResultSet) -> T): List<T> {
        val resultList = mutableListOf<T>()
        use {
            while (it.next()) {
                resultList.add(transform.invoke(it))
            }
        }
        return resultList
    }
}