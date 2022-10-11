package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.bridgeserver.BridgeServer
import lineage.vetal.server.login.clientserver.LoginClientServer

private const val TAG = "Login"
private const val PATH_SERVER_CONFIG = "login/config/Server.yaml"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("application requires data folder as first argument")
    }

    val dataFolder = args[0]
    val context = LoginContext(dataFolder)

    LoginClientServer(context).startServer()
    BridgeServer(context).startServer()

    writeInfo(TAG, "Finished Login initialization")
}