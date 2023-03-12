package lineage.vetal.server.core.db

import ch.qos.logback.classic.Logger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import lineage.vetal.server.core.ConfigDataBase
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.core.utils.logs.writeInfo
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level;
import java.sql.Connection
import kotlin.system.exitProcess

class HikariDBConnection(
    private val config: ConfigDataBase
) : DBConnection{
    private val TAG = "HikariDBConnection"
    private lateinit var databaseSource: HikariDataSource
    override val connection: Connection get() = databaseSource.connection

    init {
        initializeLoggers()
        initializePool()
        testConnection()
    }

    private fun initializeLoggers() {
        // TODO need logger configuration for this
        (LoggerFactory.getLogger("com.zaxxer.hikari") as Logger?)?.let {
            it.level = Level.OFF
        }

        (LoggerFactory.getLogger("org.mariadb.jdbc") as Logger?)?.let {
            it.level = Level.OFF
        }
    }

    private fun initializePool() {
        databaseSource = HikariDataSource(HikariConfig().apply {
            username = config.user
            password = config.password
            connectionTimeout = config.timeOut
            driverClassName = config.driver
            jdbcUrl = config.url
            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        })
    }

    private fun testConnection() {
        try {
            databaseSource.connection.use {
                writeInfo(TAG, "Successfully connected to DB - ${config.url}")
            }
        } catch (e: Exception) {
            writeError(TAG, "Cannot connect to DB - ${config.url}", e)
            exitProcess(1)
        }
    }
}