package lineage.vetal.server.login

import lineage.vetal.server.core.db.HikariDBConnection
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.db.LoginDatabase
import lineage.vetal.server.core.model.RegisteredServer
import java.io.File

private const val PATH_SERVER_CONFIG = "login/config/Server.yaml"
private const val TAG = "LoginContext"

class LoginContext {
    lateinit var loginConfig: ConfigLoginServer
    lateinit var loginDatabase: LoginDatabase
    lateinit var loginLobby: LoginLobby
    lateinit var bridgeLobby: BridgeLobby

    fun load(dataFolder: String) {
        writeSection(TAG)
        writeInfo(TAG, "Loading context. Data folder is $dataFolder")

        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading login server configs from ${serverConfigFile.absolutePath}")
        loginConfig = ConfigLoginServer.read(serverConfigFile)

        writeInfo(TAG, "Initializing database")
        val dbConnection = HikariDBConnection(loginConfig.dataBaseConfig)
        dbConnection.testConnection()
        loginDatabase = LoginDatabase(dbConnection)
        writeInfo(TAG, "DB initialized successfully")

        writeInfo(TAG, "Initializing Login lobby")
        val registeredServers = loginConfig.registeredServers.map { RegisteredServer(it) }
        loginLobby = LoginLobby(loginConfig.lobbyConfig, loginDatabase, registeredServers)
        bridgeLobby = BridgeLobby(registeredServers)
    }
}