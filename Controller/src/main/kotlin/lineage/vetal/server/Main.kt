package lineage.vetal.server

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import vetal.server.sock.*
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class NetClient(override val connection: SockConnection) : SockClient()

class NetClientFactory : SockClientFactory<NetClient> {
    override fun createClient(selector: Selector, socket: SocketChannel): NetClient {
        val selectionKey = socket.register(selector, SelectionKey.OP_READ)
        val clientConnection = SockConnection(socket, selector, selectionKey, NetPacketParser())
        return NetClient(clientConnection).also {
            selectionKey.attach(it)
        }
    }
}

class NetPacketParser : SockPacketFactory {
    override fun parsePacket(opCode: Byte, size: Int, buffer: ByteBuffer): ReadablePacket? {
        return when (opCode.toInt()) {
            0x01 -> RequestAuth()
            0x02 -> RequestAction()
            else -> null
        }
    }
}


fun main(args: Array<String>) {
    val selector: SockSelector<NetClient> =
        SockSelector("192.168.0.137", 3456, NetClientFactory(), true, TAG = "ClientSelector")
    var controller: NetClient? = null
    var plane: NetClient? = null

    runBlocking {
        launch {
            launch {
                selector.connectionAcceptFlow.collect {
                    System.err.println("Client Connected. ")
                }
            }

            launch {
                selector.connectionCloseFlow.collect {
                    System.err.println("Client Disconnected.")
                    if (it.clientId == plane?.clientId) {
                        plane = null
                    } else if (it.clientId == controller?.clientId) {
                        controller = null
                    }
                }
            }

            launch {
                selector.connectionReadFlow.collect {
                    val packet = it.second
                    System.err.println("New Packet. Packet = $packet")
                    when (packet) {
                        is RequestAction -> {
                            if (it.first.clientId == controller?.clientId) {
                                plane?.sendPacket(
                                    Action(
                                        packet.speed,
                                        packet.bottomPressed,
                                        packet.topPressed,
                                        packet.leftPressed,
                                        packet.rightPressed
                                    )
                                )
                            }
                        }

                        is RequestAuth -> {
                            if (packet.isController) {
                                controller = it.first
                            } else {
                                plane = it.first
                            }
                        }
                    }
                }
            }
        }

        System.err.println("Run blocking finished")
        selector.start()
    }
}