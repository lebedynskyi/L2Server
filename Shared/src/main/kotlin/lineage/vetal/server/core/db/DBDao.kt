package lineage.vetal.server.core.db

import java.sql.PreparedStatement
import java.sql.ResultSet

abstract class DBDao(
    private val dataBase: DBConnection
) {
    fun insertOrUpdate(sql: String, block: (PreparedStatement) -> Unit = {}): Boolean {
        return dataBase.connection.use {
            it.prepareStatement(sql)
                .apply(block)
                .executeUpdate()
        } > 0
    }

    fun <T> querySingle(sql: String, onPrepare: ((PreparedStatement) -> Unit)? = null, onTransform: (ResultSet) -> T): T? {
        return dataBase.connection.use { connection ->
            connection.prepareStatement(sql).apply {
                onPrepare?.invoke(this)
            }.executeQuery().use {
                it.firstOrNull(onTransform)
            }
        }
    }

    fun <T> queryList(sql: String, onPrepare: ((PreparedStatement) -> Unit)? = null, onTransform: (ResultSet) -> T): List<T> {
        return dataBase.connection.use { connection ->
            connection.prepareStatement(sql).apply {
                onPrepare?.invoke(this)
            }.executeQuery().use {
                it.listOrEmpty(onTransform)
            }
        }
    }
}

private fun <T> ResultSet.firstOrNull(onTransform: (ResultSet) -> T): T? {
    var result: T? = null
    use {
        if (it.next()) {
            result = onTransform.invoke(it)
        }
    }

    return result
}

private fun <T> ResultSet.listOrEmpty(onTransform: (ResultSet) -> T): List<T> {
    val resultList = mutableListOf<T>()
    use {
        while (it.next()) {
            resultList.add(onTransform.invoke(it))
        }
    }

    return resultList
}