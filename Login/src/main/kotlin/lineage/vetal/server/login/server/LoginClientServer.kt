package lineage.vetal.server.login.server

import com.vetalll.core.encryption.CryptUtil
import lineage.vetal.server.core.server.*
import lineage.vetal.server.core.settings.NetworkConfig
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import java.security.KeyPair

private val TAG = "LoginClientServer"

class LoginClientServer(
    private val networkSettings: NetworkConfig,
) {
    private lateinit var blowFishKeys: Array<ByteArray>
    private lateinit var rsaPairs: Array<KeyPair>

    suspend fun startServer() {
        blowFishKeys = Array(16) { CryptUtil.generateByteArray(16) }
        writeInfo(TAG, "Generated ${blowFishKeys.size} blowfish keys")

        rsaPairs = Array(16) { CryptUtil.generateRsa128PublicKeyPair() }
        writeInfo(TAG, "Generated ${rsaPairs.size} rsa keys")

        val filter = SocketConnectionFilter(emptyList())
        val connectionFactory = LoginClientFactory(filter, blowFishKeys, rsaPairs)
        val selectorServer = SocketSelectorThread<LoginClient>(networkSettings, connectionFactory)
        selectorServer.start()
        selectorServer.connectionAcceptFlow.collect {
            writeDebug(TAG, "New client")
        }
    }
}