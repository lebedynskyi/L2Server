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
    packetParser: PacketParser,
    private val crypt: GameConnectionCrypt,
) : ClientConnection(
    socket, selector, selectionKey, packetParser, crypt
) {
    fun sendInitPacket() {
        sendPacket(CryptInit(crypt.key))
    }
}