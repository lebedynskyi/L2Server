package lineage.vetal.server.login

import kotlinx.coroutines.runBlocking
import lineage.vetal.server.core.logs.writeDebug
import lineage.vetal.server.core.logs.writeSection
import lineage.vetal.server.core.model.ConfigIpAddress
import lineage.vetal.server.login.server.LoginClientServer

private const val TAG = "Login MAIN"

fun main() {
    writeSection(TAG)
    val server = LoginClientServer(ConfigIpAddress("127.0.0.1", 2235))

    runBlocking {
        server.startServer()
    }
    writeDebug(TAG, "After start")
}