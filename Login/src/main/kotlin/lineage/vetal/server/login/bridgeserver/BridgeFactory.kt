package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.client.*
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class BridgeFactory : ClientFactory<BridgeClient> {
    override fun createClient(selector: Selector, socket: SocketChannel): BridgeClient {
        val key = socket.register(selector, SelectionKey.OP_CONNECT or SelectionKey.OP_READ)
        val address = socket.remoteAddress as InetSocketAddress
        val crypt = BridgeCrypt()
        val parser = BridgePacketParser(crypt)
        val connection = BridgeConnection(crypt, socket, key, address, parser)
        return BridgeClient(connection).apply {
            key.attach(this)
        }
    }
}