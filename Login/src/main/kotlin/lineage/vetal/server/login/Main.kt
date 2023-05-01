package lineage.vetal.server.login

import lineage.vetal.server.login.bridgeserver.BridgeServer
import lineage.vetal.server.login.clientserver.LoginClientServer

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("application requires data folder as first argument")
    }

    val dataFolder = args[0]
    val context = LoginContext(dataFolder)

//    LoginClientServer(context).startServer()
    BridgeServer(context).startServer()
}