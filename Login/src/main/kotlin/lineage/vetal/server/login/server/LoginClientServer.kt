package lineage.vetal.server.login.server

import com.vetalll.core.encryption.CryptUtil
import lineage.vetal.server.core.server.*
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.LoginLobby
import lineage.vetal.server.login.settings.LoginConfig
import java.security.KeyPair

private val TAG = "LoginClientServer"

class LoginClientServer(
    private val loginServerConfig: LoginConfig
) {
    private val loginLobby = LoginLobby(loginServerConfig.lobbyConfig)
    private val blowFishKeys: Array<ByteArray>
    private val rsaPairs: Array<KeyPair>
    private val filter: SocketConnectionFilter
    private val connectionFactory: LoginClientFactory

    private var selectorServer: SocketSelectorThread<LoginClient>? = null

    init {
        writeSection(TAG)
        blowFishKeys = Array(16) { CryptUtil.generateByteArray(16) }
        writeInfo(TAG, "Generated ${blowFishKeys.size} blowfish keys")

        rsaPairs = Array(16) { CryptUtil.generateRsa128PublicKeyPair() }
        writeInfo(TAG, "Generated ${rsaPairs.size} rsa keys")

        filter = SocketConnectionFilter(emptyList())
        connectionFactory = LoginClientFactory(filter, blowFishKeys, rsaPairs)
    }

    suspend fun startServer() {
        selectorServer = SocketSelectorThread(loginServerConfig.clientServer, connectionFactory)
        selectorServer?.start()
        selectorServer?.connectionAcceptFlow?.collect {
            loginLobby.onClientConnected(it)
        }
    }
}