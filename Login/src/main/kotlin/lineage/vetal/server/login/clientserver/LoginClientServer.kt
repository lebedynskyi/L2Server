package lineage.vetal.server.login.clientserver

import kotlinx.coroutines.*
import lineage.vetal.server.core.encryption.CryptUtil
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket
import lineage.vetal.server.login.clientserver.packets.client.ClientConnected
import lineage.vetal.server.login.clientserver.packets.client.ClientDisconnected
import vetal.server.sock.SockSelector
import java.security.KeyPair

class LoginClientServer(
    private val context: LoginContext
) {
    private val TAG = "LoginClientServer"
    private val blowFishKeys: Array<ByteArray>
    private val rsaPairs: Array<KeyPair>
    private val clientFactory: LoginClientFactory

    private var selectorThread: SockSelector<LoginClient>
    private val serverScope = CoroutineScope(Dispatchers.IO + Job())

    init {
        writeSection(TAG)
        blowFishKeys = Array(32) { CryptUtil.generateByteArray(16) }
        writeInfo(TAG, "Generated ${blowFishKeys.size} blowfish keys")

        rsaPairs = Array(32) { CryptUtil.generateRsa128PublicKeyPair() }
        writeInfo(TAG, "Generated ${rsaPairs.size} rsa keys")

        clientFactory = LoginClientFactory(blowFishKeys, rsaPairs)

        selectorThread = SockSelector(
            context.loginConfig.clientServer.hostname,
            context.loginConfig.clientServer.port,
            clientFactory,
            isServer = true,
            TAG = "LoginClientSelector"
        )
    }

    fun startServer() {
        selectorThread.start()

        // TODO error handler ? Try catch ? some hierarchy of errors?

        serverScope.launch {
            selectorThread.connectionAcceptFlow.collect {
                val packet = ClientConnected()
                packet.execute(it, context)
            }
        }

        serverScope.launch {
            selectorThread.connectionCloseFlow.collect {
                val packet = ClientDisconnected()
                packet.execute(it, context)
            }
        }

        serverScope.launch {
            selectorThread.connectionReadFlow.collect {
                val packet = it.second as LoginClientPacket?
                packet?.execute(it.first, context)
            }
        }
    }
}