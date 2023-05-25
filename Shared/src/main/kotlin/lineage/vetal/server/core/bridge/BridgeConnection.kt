package lineage.vetal.server.core.bridge

import vetal.server.network.ClientConnection
import vetal.server.network.PacketParser
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class BridgeConnection(
    val crypt: BridgeConnectionCrypt,
    socket: SocketChannel,
    selector: Selector,
    selectionKey: SelectionKey,
    packetParser: PacketParser,
) : ClientConnection(socket, selector, selectionKey, packetParser, crypt)