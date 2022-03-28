package lineage.vetal.server.core.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import lineage.vetal.server.core.config.DataBaseConfig
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.core.utils.logs.writeInfo
import java.sql.Connection

class DBConnection(
    config: DataBaseConfig
) {
    private val TAG = "DataBase"
    private val databaseSource = HikariDataSource(HikariConfig().apply {
        jdbcUrl = config.url
        username = config.user;
        password = config.password
        addDataSourceProperty("cachePrepStmts", "true");
        addDataSourceProperty("prepStmtCacheSize", "250");
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    })

    init {
        try {
            val c = databaseSource.connection
            writeInfo(TAG, "Successfuly connected to DB - ${config.url}")
            c.close()
        } catch (e: Exception) {
            writeError(TAG, "Cannot connect to DB - ${config.url}", e)
        }
    }

    internal fun getConnection(): Connection = databaseSource.connection
}