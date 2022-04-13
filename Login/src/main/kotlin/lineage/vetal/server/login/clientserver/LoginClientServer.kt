package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.encryption.CryptUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import lineage.vetal.server.core.server.*
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.packets.client.ClientConnected
import java.security.KeyPair

class LoginClientServer(
    private val context: LoginContext
) {
    private val TAG = "LoginClientServer"
    private val blowFishKeys: Array<ByteArray>
    private val rsaPairs: Array<KeyPair>
    private val connectionFactory: LoginClientFactory

    private lateinit var selectorThread: SelectorServerThread<LoginClient>
    private val serverContext = newSingleThreadContext("Login")
    private val loginPacketHandler = LoginClientPacketHandler(context)

    init {
        writeSection(TAG)
        blowFishKeys = Array(32) { CryptUtil.generateByteArray(16) }
        writeInfo(TAG, "Generated ${blowFishKeys.size} blowfish keys")

        rsaPairs = Array(32) { CryptUtil.generateRsa128PublicKeyPair() }
        writeInfo(TAG, "Generated ${rsaPairs.size} rsa keys")

        connectionFactory = LoginClientFactory(blowFishKeys, rsaPairs)
    }

    suspend fun startServer() {
        selectorThread = SelectorServerThread(context.config.clientServer, connectionFactory).apply {
            start()
        }

        withContext(serverContext) {
            launch {
                selectorThread.connectionCloseFlow.collect {
                    // TODO remove from lobby ? How to checked authed player?
                }
            }

            launch {
                selectorThread.connectionAcceptFlow.collect {
                    loginPacketHandler.handle(it, ClientConnected())
                }
            }

            launch {
                selectorThread.connectionReadFlow.collect {
                    loginPacketHandler.handle(it.first, it.second)
                }
            }
        }
    }
}