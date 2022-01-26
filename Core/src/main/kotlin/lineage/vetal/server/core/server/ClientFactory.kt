package lineage.vetal.server.core.server

import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel


interface ClientFactory<T : Client> {
    fun createClient(selector: Selector, serverSocket: ServerSocketChannel): T
}