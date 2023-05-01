package lineage.vetal.server.game.gameserver

import lineage.vetal.server.game.gameserver.packet.server.CryptInit
import vetal.server.network.ClientConnection
import vetal.server.network.PacketParser
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class GameClientConnection(
    socket: SocketChannel,
    selector: Selector,
    selectionKey: SelectionKey,
    clientAddress: InetSocketAddress,
    private val crypt: GameConnectionCrypt,
    packetParser: PacketParser,
) : ClientConnection(
    socket, selector, selectionKey, clientAddress, crypt, packetParser
) {
    fun sendInitPacket() {
        sendPacket(CryptInit(crypt.key))
    }
}