package lineage.vetal.server.login.server

import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.core.client.ClientFactory
import lineage.vetal.server.core.server.SocketConnectionFilter
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.security.KeyPair
import kotlin.random.Random

class LoginClientFactory(
    private val filter: SocketConnectionFilter,
    private val blowFishKeys: Array<ByteArray>,
    private val rsaPairs: Array<KeyPair>
) : ClientFactory<LoginClient> {

    override fun createClient(selector: Selector, serverSocket: ServerSocketChannel): LoginClient {
        val socket = serverSocket.accept().apply { configureBlocking(false) }
        val address = socket.remoteAddress as InetSocketAddress
        val crypt = LoginCrypt(blowFishKeys.random(), rsaPairs.random())
        val key = socket.register(selector, SelectionKey.OP_CONNECT)
        val clientConnection = ClientConnection(socket, key, address)
        val client = LoginClient(crypt, Random.nextInt(), clientConnection)
        key.attach(client)
        return client
    }
}