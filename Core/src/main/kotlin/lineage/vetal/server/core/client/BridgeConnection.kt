package lineage.vetal.server.core.client

import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

class BridgeConnection(
    val crypt: BridgeCrypt,
    socket: SocketChannel,
    selector: Selector,
    selectionKey: SelectionKey,
    clientAddress: InetSocketAddress,
    packetParser: PacketParser,
) : ClientConnection(socket, selector, selectionKey, clientAddress, crypt, packetParser)