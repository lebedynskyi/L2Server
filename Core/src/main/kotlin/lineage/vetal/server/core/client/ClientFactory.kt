package lineage.vetal.server.core.client

import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel


interface ClientFactory<T : Client> {
    fun createClient(selector: Selector, serverSocket: ServerSocketChannel): T
}