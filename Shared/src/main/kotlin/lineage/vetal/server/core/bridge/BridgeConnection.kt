package lineage.vetal.server.core.bridge

import vetal.server.sock.SockConnection
import vetal.server.sock.SockPacketFactory

class BridgeConnection(
    packetParser: SockPacketFactory,
    val crypt: BridgeConnectionCrypt,
) : SockConnection(packetParser, crypt)