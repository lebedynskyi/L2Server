package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.bridge.*
import vetal.server.sock.SockClientFactory
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class BridgeFactory : SockClientFactory<BridgeClient> {
    override fun createClient(selector: Selector, socket: SocketChannel): BridgeClient {
        val key = socket.register(selector, SelectionKey.OP_READ)
        val crypt = BridgeConnectionCrypt()
        val parser = BridgePacketParser()
        val connection = BridgeConnection(crypt, socket, selector, key, parser)
        return BridgeClient(connection).apply {
            key.attach(this)
        }
    }
}