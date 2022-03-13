package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.encryption.CryptUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lineage.vetal.server.core.server.*
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.LoginLobby
import lineage.vetal.server.login.config.LoginConfig
import java.security.KeyPair

class LoginClientServer(
    private val loginServerConfig: LoginConfig
) {
    private val TAG = "LoginClientServer"
    private val loginLobby = LoginLobby(loginServerConfig.lobbyConfig)
    private val blowFishKeys: Array<ByteArray>
    private val rsaPairs: Array<KeyPair>
    private val filter: SocketConnectionFilter
    private val connectionFactory: LoginClientFactory

    private var selectorThread: SocketSelectorThread<LoginClient>? = null

    init {
        writeSection(TAG)
        blowFishKeys = Array(32) { CryptUtil.generateByteArray(16) }
        writeInfo(TAG, "Generated ${blowFishKeys.size} blowfish keys")

        rsaPairs = Array(32) { CryptUtil.generateRsa128PublicKeyPair() }
        writeInfo(TAG, "Generated ${rsaPairs.size} rsa keys")

        filter = SocketConnectionFilter(emptyList())
        connectionFactory = LoginClientFactory(filter, blowFishKeys, rsaPairs)
    }

    fun startServer() {
        selectorThread = SocketSelectorThread(loginServerConfig.clientServer, connectionFactory).apply {
            start()
        }

        runBlocking {
            launch {
                selectorThread?.connectionCloseFlow?.collect {
                    loginLobby.onClientDisconnected(it)
                }
            }

            launch {
                selectorThread?.connectionAcceptFlow?.collect {
                    loginLobby.onClientConnected(it)
                }
            }

            launch {
                selectorThread?.connectionReadFlow?.collect {
                    loginLobby.onPacketReceived(it.first, it.second)
                }
            }
        }
    }
}