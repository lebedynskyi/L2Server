package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.server.ClientFactory
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.security.KeyPair
import kotlin.random.Random

class LoginClientFactory(
    private val blowFishKeys: Array<ByteArray>,
    private val rsaPairs: Array<KeyPair>
) : ClientFactory<LoginClient> {

    override fun createClient(selector: Selector, socket: SocketChannel): LoginClient {
        val address = socket.remoteAddress as InetSocketAddress
        val crypt = LoginClientCrypt(blowFishKeys.random(), rsaPairs.random())
        val key = socket.register(selector, SelectionKey.OP_READ)
        val parser = LoginClientPacketParser()
        val clientConnection = LoginClientConnection(crypt, parser, socket, selector, key, address)
        return LoginClient(Random.nextInt(), clientConnection).apply {
            key.attach(this)
        }
    }
}