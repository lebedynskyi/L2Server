package lineage.vetal.server.core.db

import ch.qos.logback.classic.Logger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import lineage.vetal.server.core.DataBaseConfig
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.core.utils.logs.writeInfo
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level;
import java.sql.Connection

class DBConnection(
    private val config: DataBaseConfig
) {
    private val TAG = "DataBase"
    private lateinit var databaseSource: HikariDataSource

    init {
        initializeLoggers()
        initializePool()
        testConnection()
    }

    internal fun getConnection(): Connection = databaseSource.connection

    private fun initializeLoggers() {
        // TODO need logger configuration for this
        val hikariLogger = LoggerFactory.getLogger("com.zaxxer.hikari") as Logger
        hikariLogger.level = Level.OFF

        val jdbcLogger = LoggerFactory.getLogger("org.mariadb.jdbc") as Logger
        jdbcLogger.level = Level.OFF
    }

    private fun initializePool() {
        databaseSource = HikariDataSource(HikariConfig().apply {
            jdbcUrl = config.url
            username = config.user;
            password = config.password
            driverClassName = "org.mariadb.jdbc.Driver"
            addDataSourceProperty("cachePrepStmts", "true");
            addDataSourceProperty("prepStmtCacheSize", "250");
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        })
    }

    private fun testConnection() {
        try {
            databaseSource.connection.use {
                writeInfo(TAG, "Successfuly connected to DB - ${config.url}")
            }
        } catch (e: Exception) {
            writeError(TAG, "Cannot connect to DB - ${config.url}", e)
        }
    }
}