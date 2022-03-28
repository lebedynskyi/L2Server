package lineage.vetal.server.login

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.bridgeserver.BridgeServer
import lineage.vetal.server.login.clientserver.LoginClientServer
import java.io.File

private const val TAG = "Login"
private const val PATH_SERVER_CONFIG = "login/config/Server.yaml"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("application requires data folder as first argument")
    }

    val dataFolder = args[0]
    writeSection(TAG)
    val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
    writeInfo(TAG, "Reading configs from ${serverConfigFile.absolutePath}")
    val config = LoginConfig.read(serverConfigFile)
    val loginLobby = LoginLobby(config.lobbyConfig, config.registeredServers)

    runBlocking {
        launch {
            LoginClientServer(loginLobby, config.clientServer).startServer()
        }
        launch {
            BridgeServer(loginLobby, config.bridgeServer).startServer()
        }
    }

    writeDebug(TAG, "Finished")
}