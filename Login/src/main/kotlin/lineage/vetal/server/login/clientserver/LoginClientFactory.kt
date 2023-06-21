package lineage.vetal.server.login.clientserver

import vetal.server.sock.SockClientFactory
import java.security.KeyPair
import kotlin.random.Random

class LoginClientFactory(
    private val blowFishKeys: Array<ByteArray>,
    private val rsaPairs: Array<KeyPair>
) : SockClientFactory<LoginClient>() {

    override fun createClient(): LoginClient {
        val crypt = LoginConnectionCrypt(blowFishKeys.random(), rsaPairs.random())
        val parser = LoginClientPacketParser()
        val clientConnection = LoginClientConnection(parser, crypt)
        return LoginClient(Random.nextInt(), clientConnection)
    }
}