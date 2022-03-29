package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.db.LoginDatabase
import java.io.File

private const val PATH_SERVER_CONFIG = "login/config/Server.yaml"


class LoginContext(
    private val dataFolder: String
) {
    private val TAG = "LoginContext"

    var config: LoginConfig
    val loginDatabase: LoginDatabase
    val loginLobby: LoginLobby

    init {
        writeSection(TAG)
        writeInfo(TAG, "Reading configs")
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")

        config = LoginConfig.read(serverConfigFile)
        loginLobby = LoginLobby(config.lobbyConfig, config.registeredServers)

        writeInfo(TAG, "Initialize database")
        loginDatabase = LoginDatabase(config.dataBaseConfig)
    }
}