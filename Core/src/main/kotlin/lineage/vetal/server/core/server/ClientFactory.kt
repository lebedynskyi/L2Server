package lineage.vetal.server.core.server

import java.nio.channels.Selector
import java.nio.channels.SocketChannel


interface ClientFactory<T: Client> {
    fun createClient(selector: Selector, socket: SocketChannel): T
}