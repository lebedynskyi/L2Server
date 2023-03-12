package lineage.vetal.server.login

import lineage.vetal.server.core.db.HikariDBConnection
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.db.LoginDatabase
import lineage.vetal.server.core.model.RegisteredServer
import java.io.File

private const val PATH_SERVER_CONFIG = "login/config/Server.yaml"
private const val TAG = "LoginContext"

class LoginContext(
    dataFolder: String
) {
    var config: ConfigLogin
    val loginDatabase: LoginDatabase
    val loginLobby: LoginLobby

    init {
        writeSection(TAG)
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading login server configs from ${serverConfigFile.absolutePath}")

        config = ConfigLogin.read(serverConfigFile)
        loginLobby = LoginLobby(config.lobbyConfig, config.registeredServers.map { RegisteredServer(it) })

        writeInfo(TAG, "Initializing database")
        val dbConnection = HikariDBConnection(config.dataBaseConfig)
        loginDatabase = LoginDatabase(dbConnection)
        writeInfo(TAG, "DB initialized successfully")
    }
}