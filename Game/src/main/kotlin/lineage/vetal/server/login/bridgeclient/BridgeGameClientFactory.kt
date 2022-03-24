package lineage.vetal.server.login.bridgeclient

import lineage.vetal.server.core.client.BridgeConnection
import lineage.vetal.server.core.client.BridgeCrypt
import lineage.vetal.server.core.client.ClientFactory
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class BridgeGameClientFactory : ClientFactory<BridgeGameClient> {
    override fun createClient(selector: Selector, socket: SocketChannel): BridgeGameClient {
        val address = socket.remoteAddress as InetSocketAddress
        val key = socket.register(selector, SelectionKey.OP_READ or SelectionKey.OP_CONNECT)
        val crypt = BridgeCrypt()
        val parser = BridgeGamePacketParser()
        val connection = BridgeConnection(crypt, socket, selector, key, address, parser)
        return BridgeGameClient(connection).apply {
            key.attach(this)
        }
    }
}