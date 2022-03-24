import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.concurrent.CountDownLatch


val address = InetSocketAddress("localhost", 5454)

fun main() {
    val serverSocketSignal = CountDownLatch(1)

    Thread {
        startServer(serverSocketSignal)
    }.start()

    Thread {
        startClient(serverSocketSignal)
    }.start()
}

fun startServer(serverSocketSignal: CountDownLatch) {
    //prepare server socket
    val selector = Selector.open()
    val serverSocket = ServerSocketChannel.open().apply {
        socket().bind(address)
        configureBlocking(false)
        register(selector, SelectionKey.OP_ACCEPT)
    }

    serverSocketSignal.countDown();

    //run server loop
    while (true) {
        println("Server loop")
        val readyChannels = selector.select()
        if (readyChannels == 0) continue

        val keyIterator = selector.selectedKeys().iterator()
        while (keyIterator.hasNext()) {
            val key = keyIterator.next()
            when (key.readyOps()) {
                SelectionKey.OP_ACCEPT -> {
                    println("Server ACCEPT")
                    val socket = serverSocket.accept().apply {
                        configureBlocking(false)
                    }
                    val clientKey = socket.register(selector, SelectionKey.OP_READ)

                    Thread{
                        Thread.sleep(3000)
                        println("Server setting interestedOps to OP_WRITE from new thread")
                        clientKey.interestOps(SelectionKey.OP_WRITE)
                    }.start()
                }
                SelectionKey.OP_READ -> {
                    print("Server READ")
                    val buffer = ByteBuffer.allocate(1024)
                    val count = (key.channel() as SocketChannel).read(buffer)
                    val message = String(buffer.array(), 0, count)
                    println(" $message")
                }

                SelectionKey.OP_WRITE -> {
                    println("Server WRITE")
                    val buffer = ByteBuffer.wrap("Hello worm server".toByteArray());
                    (key.channel() as SocketChannel).write(buffer)
                    key.interestOps(0)
                }
            }
            keyIterator.remove()
        }
    }
}

fun startClient(serverSocketSignal: CountDownLatch) {
    serverSocketSignal.await();

    //prepare client socket
    val selector = Selector.open()
    SocketChannel.open().apply {
        configureBlocking(false)
        connect(address)
        register(selector, SelectionKey.OP_CONNECT or SelectionKey.OP_READ)
    }

    //run client loop
    while (true) {
        println("Client loop")
        val readyChannels = selector.select()
        if (readyChannels == 0) continue

        val keyIterator = selector.selectedKeys().iterator()
        while (keyIterator.hasNext()) {
            val key = keyIterator.next()
            when (key.readyOps()) {
                SelectionKey.OP_CONNECT -> {
                    println("Client CONNECT")
                    val socket = (key.channel() as SocketChannel)
                    socket.finishConnect()
                    key.interestOps(key.interestOps() and SelectionKey.OP_CONNECT.inv())
                }
                SelectionKey.OP_WRITE -> {
                    println("Client WRITE")
                    val buffer = ByteBuffer.wrap("Hello worm client".toByteArray());
                    (key.channel() as SocketChannel).write(buffer)
                    key.interestOps(0)
                }

            }
            keyIterator.remove()
        }
    }
}