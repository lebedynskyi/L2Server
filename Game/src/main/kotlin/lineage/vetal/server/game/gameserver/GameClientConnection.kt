package lineage.vetal.server.game.gameserver

import lineage.vetal.server.game.gameserver.packet.server.CryptInit
import vetal.server.sock.SockConnection
import vetal.server.sock.SockPacketFactory
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class GameClientConnection(
    socket: SocketChannel,
    selector: Selector,
    selectionKey: SelectionKey,
    packetParser: SockPacketFactory,
    private val crypt: GameConnectionCrypt,
) : SockConnection(
    socket, selector, selectionKey, packetParser, crypt
) {
    fun sendInitPacket() {
        sendPacket(CryptInit(crypt.key))
    }
}