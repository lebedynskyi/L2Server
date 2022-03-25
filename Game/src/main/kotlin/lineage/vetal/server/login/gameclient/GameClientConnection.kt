package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.core.client.PacketParser
import lineage.vetal.server.login.gameclient.packet.server.CryptInit
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class GameClientConnection(
    socket: SocketChannel,
    selector: Selector,
    selectionKey: SelectionKey,
    clientAddress: InetSocketAddress,
    private val crypt: GameClientCrypt,
    packetParser: PacketParser,
) : ClientConnection(
    socket, selector, selectionKey, clientAddress, crypt, packetParser
) {
    fun sendInitPacket() {
        sendPacket(CryptInit(crypt.key))
    }
}