package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.encryption.CryptUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import lineage.vetal.server.core.NetworkConfig
import lineage.vetal.server.core.server.*
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.LoginLobby
import java.security.KeyPair

class LoginClientServer(
    private val loginLobby: LoginLobby,
    private val clientServer: NetworkConfig
) {
    private val TAG = "LoginClientServer"
    private val blowFishKeys: Array<ByteArray>
    private val rsaPairs: Array<KeyPair>
    private val filter: SocketConnectionFilter
    private val connectionFactory: LoginClientFactory

    private lateinit var selectorThread: SelectorServerThread<LoginClient>
    private val serverContext = newSingleThreadContext("Login")

    init {
        writeSection(TAG)
        blowFishKeys = Array(32) { CryptUtil.generateByteArray(16) }
        writeInfo(TAG, "Generated ${blowFishKeys.size} blowfish keys")

        rsaPairs = Array(32) { CryptUtil.generateRsa128PublicKeyPair() }
        writeInfo(TAG, "Generated ${rsaPairs.size} rsa keys")

        filter = SocketConnectionFilter(emptyList())
        connectionFactory = LoginClientFactory(filter, blowFishKeys, rsaPairs)
    }

    suspend fun startServer() {
        selectorThread = SelectorServerThread(clientServer, connectionFactory).apply {
            start()
        }

        withContext(serverContext) {
            launch {
                selectorThread.connectionCloseFlow.collect {
                    loginLobby.onClientDisconnected(it)
                }
            }

            launch {
                selectorThread.connectionAcceptFlow.collect {
                    loginLobby.onClientConnected(it)
                }
            }

            launch {
                selectorThread.connectionReadFlow.collect {
                    loginLobby.onClientPacketReceived(it.first, it.second)
                }
            }
        }
    }
}