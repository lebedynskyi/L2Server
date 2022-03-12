package lineage.vetal.server.login

import kotlinx.coroutines.runBlocking
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.core.utils.streams.openResource
import lineage.vetal.server.login.server.LoginClientServer
import lineage.vetal.server.login.settings.LoginServerConfig

private const val TAG = "Login"
private const val PATH_SERVER_CONFIG = "config/Server.yaml"

fun main() {
    writeSection(TAG)
    writeInfo(TAG, "Reading configs from $PATH_SERVER_CONFIG")

    val configInputStream = openResource(PATH_SERVER_CONFIG)
    val config = LoginServerConfig.read(configInputStream)
    val server = LoginClientServer(config.clientServer)

    runBlocking {
        server.startServer()
    }
    writeDebug(TAG, "Finished")
}