package vetal.server.sock

import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

abstract class SockClientFactory<T : SockClient> {
    open fun createClient(selector: Selector, socket: SocketChannel): T {
        val selectionKey = socket.register(selector, SelectionKey.OP_READ)
        return createClient().apply {
            this.connection.socket = socket
            this.connection.selector = selector
            this.connection.selectionKey = selectionKey
            selectionKey.attach(this)
        }
    }

    open fun createClient(): T {
        throw NotImplementedError("SockClientFactory.createClient is not implemented. You need provide your implementation to support receiving packets")
    }
}