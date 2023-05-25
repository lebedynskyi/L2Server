package lineage.vetal.server.core.bridge

import vetal.server.sock.SockConnection
import vetal.server.sock.SockPacketFactory
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class BridgeConnection(
    val crypt: BridgeConnectionCrypt,
    socket: SocketChannel,
    selector: Selector,
    selectionKey: SelectionKey,
    packetParser: SockPacketFactory,
) : SockConnection(socket, selector, selectionKey, packetParser, crypt)