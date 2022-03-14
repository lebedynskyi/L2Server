package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.client.ClientFactory
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel

class BridgeFactory : ClientFactory<BridgeClient>{
    override fun createClient(selector: Selector, serverSocket: ServerSocketChannel): BridgeClient {
        TODO("Not yet implemented")
    }
}