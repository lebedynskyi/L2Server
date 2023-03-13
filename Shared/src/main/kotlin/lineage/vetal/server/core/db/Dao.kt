package lineage.vetal.server.core.db

import java.sql.PreparedStatement
import java.sql.ResultSet

abstract class Dao(
    private val dataBase: DBConnection
) {
    fun insertOrUpdate(sql: String, block: (PreparedStatement) -> Unit = {}): Boolean {
        return dataBase.connection.use {
            it.prepareStatement(sql)
                .apply(block)
                .executeUpdate()
        } > 0
    }

    fun <T> querySingle(sql: String, prepare: ((PreparedStatement) -> Unit)? = null, transform: (ResultSet) -> T): T? {
        return dataBase.connection.use { connection ->
            connection.prepareStatement(sql).apply {
                prepare?.invoke(this)
            }.executeQuery().use {
                it.firstOrNull(transform)
            }
        }
    }

    fun <T> queryList(sql: String, prepare: ((PreparedStatement) -> Unit)? = null, transform: (ResultSet) -> T): List<T> {
        return dataBase.connection.use { connection ->
            connection.prepareStatement(sql).apply {
                prepare?.invoke(this)
            }.executeQuery().use {
                it.listOrEmpty(transform)
            }
        }
    }
}

private fun <T> ResultSet.firstOrNull(transform: (ResultSet) -> T): T? {
    var result: T? = null
    use {
        if (it.next()) {
            result = transform.invoke(it)
        }
    }

    return result
}

private fun <T> ResultSet.listOrEmpty(transform: (ResultSet) -> T): List<T> {
    val resultList = mutableListOf<T>()
    use {
        while (it.next()) {
            resultList.add(transform.invoke(it))
        }
    }

    return resultList
}