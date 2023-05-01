package lineage.vetal.server.game.bridgeclient

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.bridge.BridgeConnection
import lineage.vetal.server.core.bridge.BridgeConnectionCrypt
import vetal.server.network.ClientFactory
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class BridgeGameClientFactory : ClientFactory<BridgeClient> {
    override fun createClient(selector: Selector, socket: SocketChannel): BridgeClient {
        val address = socket.remoteAddress as InetSocketAddress
        val selectionKey = socket.register(selector, SelectionKey.OP_READ)
        val crypt = BridgeConnectionCrypt()
        val parser = BridgeGamePacketParser()
        val connection = BridgeConnection(crypt, socket, selector, selectionKey, address, parser)
        return BridgeClient(connection).also {
            selectionKey.attach(it)
        }
    }
}