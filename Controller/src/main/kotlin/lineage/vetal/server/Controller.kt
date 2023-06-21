package lineage.vetal.server

import vetal.server.sock.*
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class Controller {
    var controller: SockClient? = null
    var plane: SockClient? = null

    fun onClientDisconnected(it: SockClient) {
        if (it.clientId == plane?.clientId) {
            plane = null
        } else if (it.clientId == controller?.clientId) {
            controller = null
        }
    }

    fun onPacketReceived(client: SockClient, packet: ReadablePacket) {
        when (client.clientId) {
            controller?.clientId -> onControllerPacketReceived(client, packet)
            plane?.clientId -> onPlanePacketReceived(client, packet)
            else -> onUnknownPacketReceived(client, packet)
        }
    }

    private fun onUnknownPacketReceived(client: SockClient, packet: ReadablePacket) {
        if (packet is RequestAuth) {
            if (packet.isController) {
                controller = client
                controller?.sendPacket(Info(plane !== null))
            } else {
                plane = client
            }
        }
    }

    private fun onPlanePacketReceived(client: SockClient, packet: ReadablePacket) {
        // ADD some ping here ?
    }

    private fun onControllerPacketReceived(client: SockClient, packet: ReadablePacket) {
        when (packet) {
            is RequestAction -> {
                if (client.clientId == controller?.clientId) {
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
        }
    }
}

class ControllerSockClientFactory : SockClientFactory<SockClient>() {
    override fun createClient(): SockClient {
        val clientConnection = SockConnection(ControllerPacketParser())
        return SockClient(clientConnection)
    }
}

class ControllerPacketParser : SockPacketFactory {
    override fun parsePacket(opCode: Byte, size: Int, buffer: ByteBuffer): ReadablePacket? {
        return when (opCode.toInt()) {
            0x01 -> RequestAuth()
            0x02 -> RequestAction()
            else -> null
        }
    }
}