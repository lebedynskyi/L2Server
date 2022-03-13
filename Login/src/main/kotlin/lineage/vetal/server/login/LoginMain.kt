package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.core.utils.streams.openResource
import lineage.vetal.server.login.clientserver.LoginClientServer
import lineage.vetal.server.login.config.LoginConfig

private const val TAG = "Login"
private const val PATH_SERVER_CONFIG = "config/Server.yaml"

fun main() {
    writeSection(TAG)
    writeInfo(TAG, "Reading configs from $PATH_SERVER_CONFIG")

    val configInputStream = openResource(PATH_SERVER_CONFIG)
    val config = LoginConfig.read(configInputStream)
    val server = LoginClientServer(config)

    server.startServer()
    writeDebug(TAG, "Finished")
}