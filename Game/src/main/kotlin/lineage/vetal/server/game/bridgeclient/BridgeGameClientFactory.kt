package lineage.vetal.server.game.bridgeclient

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.bridge.BridgeConnection
import lineage.vetal.server.core.bridge.BridgeConnectionCrypt
import vetal.server.sock.SockClientFactory
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class BridgeGameClientFactory : SockClientFactory<BridgeClient> {
    override fun createClient(selector: Selector, socket: SocketChannel): BridgeClient {
        val selectionKey = socket.register(selector, SelectionKey.OP_READ)
        val crypt = BridgeConnectionCrypt()
        val parser = BridgeGamePacketParser()
        val connection = BridgeConnection(crypt, socket, selector, selectionKey, parser)
        return BridgeClient(connection).also {
            selectionKey.attach(it)
        }
    }
}