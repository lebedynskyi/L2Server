package lineage.vetal.server.core.bridge

import vetalll.server.sock.SockConnection
import vetalll.server.sock.SockPacketFactory

class BridgeConnection(
    packetParser: SockPacketFactory,
    val crypt: BridgeConnectionCrypt,
) : SockConnection(packetParser, crypt)