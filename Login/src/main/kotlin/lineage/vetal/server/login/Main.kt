package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.bridgeserver.BridgeServer
import lineage.vetal.server.login.clientserver.LoginClientServer

private const val TAG = "LoginMain"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("application requires data folder as first argument")
    }

    writeInfo(TAG, "Create and load context")
    val dataFolder = args[0]
    val context = LoginContext().apply {
        load(dataFolder)
    }

    writeInfo(TAG, "Start login client server")
    LoginClientServer(context).startServer()

    writeInfo(TAG, "Start login bridge server")
    BridgeServer(context).startServer()

    writeInfo(TAG, "Finish login initialization")
}