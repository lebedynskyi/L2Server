package lineage.vetal.server.login.clientserver

import vetal.server.sock.SockClientFactory
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.security.KeyPair
import kotlin.random.Random

class LoginClientFactory(
    private val blowFishKeys: Array<ByteArray>,
    private val rsaPairs: Array<KeyPair>
) : SockClientFactory<LoginClient> {

    override fun createClient(selector: Selector, socket: SocketChannel): LoginClient {
        val crypt = LoginConnectionCrypt(blowFishKeys.random(), rsaPairs.random())
        val selectionKey = socket.register(selector, SelectionKey.OP_READ)
        val parser = LoginClientPacketParser()
        val clientConnection = LoginClientConnection(crypt, parser, socket, selector, selectionKey)
        return LoginClient(Random.nextInt(), clientConnection).also {
            selectionKey.attach(it)
        }
    }
}