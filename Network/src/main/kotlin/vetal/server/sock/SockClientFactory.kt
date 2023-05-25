package vetal.server.sock

import java.nio.channels.Selector
import java.nio.channels.SocketChannel

interface SockClientFactory<T : SockClient> {
    fun createClient(selector: Selector, socket: SocketChannel): T
}