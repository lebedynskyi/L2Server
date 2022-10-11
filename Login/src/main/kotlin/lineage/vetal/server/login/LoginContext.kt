package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.db.LoginDatabase
import lineage.vetal.server.core.model.ServerInfo
import java.io.File

private const val PATH_SERVER_CONFIG = "login/config/Server.yaml"


class LoginContext(
    private val dataFolder: String
) {
    private val TAG = "LoginContext"

    var config: ConfigLogin
    val loginDatabase: LoginDatabase
    val loginLobby: LoginLobby

    init {
        writeSection(TAG)
        writeInfo(TAG, "Reading configs")
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")

        config = ConfigLogin.read(serverConfigFile)
        loginLobby = LoginLobby(config.lobbyConfig, config.registeredServers.map { ServerInfo(it) })

        writeInfo(TAG, "Initialize database")
        loginDatabase = LoginDatabase(config.dataBaseConfig)
    }
}