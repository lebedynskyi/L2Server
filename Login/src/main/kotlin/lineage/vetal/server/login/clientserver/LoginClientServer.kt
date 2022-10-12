package lineage.vetal.server.login.clientserver

import kotlinx.coroutines.*
import lineage.vetal.server.core.encryption.CryptUtil
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.packets.client.ClientConnected
import lineage.vetal.server.login.clientserver.packets.client.ClientDisconnected
import vetal.server.network.SelectorThread
import java.security.KeyPair

class LoginClientServer(
    private val context: LoginContext
) {
    private val TAG = "LoginClientServer"
    private val blowFishKeys: Array<ByteArray>
    private val rsaPairs: Array<KeyPair>
    private val connectionFactory: LoginClientFactory

    private var selectorThread: SelectorThread<LoginClient>
    private val serverScope = CoroutineScope(Dispatchers.IO + Job())
    private val loginPacketHandler = LoginClientPacketHandler(context)

    init {
        writeSection(TAG)
        blowFishKeys = Array(32) { CryptUtil.generateByteArray(16) }
        writeInfo(TAG, "Generated ${blowFishKeys.size} blowfish keys")

        rsaPairs = Array(32) { CryptUtil.generateRsa128PublicKeyPair() }
        writeInfo(TAG, "Generated ${rsaPairs.size} rsa keys")

        connectionFactory = LoginClientFactory(blowFishKeys, rsaPairs)

        selectorThread = SelectorThread(
            context.config.clientServer.hostname,
            context.config.clientServer.port,
            connectionFactory,
            TAG = "LoginClientSelector"
        )
    }

    fun startServer() {
        selectorThread.start()

        serverScope.launch {
            selectorThread.connectionCloseFlow.collect {
                loginPacketHandler.handle(it, ClientDisconnected())
            }
        }

        serverScope.launch {
            selectorThread.connectionAcceptFlow.collect {
                loginPacketHandler.handle(it, ClientConnected())
            }
        }

        serverScope.launch {
            selectorThread.connectionReadFlow.collect {
                loginPacketHandler.handle(it.first, it.second)
            }
        }
    }
}