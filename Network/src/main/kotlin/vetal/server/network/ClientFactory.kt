package vetal.server.network

import java.nio.channels.Selector
import java.nio.channels.SocketChannel


interface ClientFactory<T: Client> {
    fun createClient(selector: Selector, socket: SocketChannel): T
}