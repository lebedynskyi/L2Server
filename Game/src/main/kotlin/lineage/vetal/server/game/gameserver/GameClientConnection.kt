package lineage.vetal.server.game.gameserver

import lineage.vetal.server.game.gameserver.packet.server.CryptInit
import vetalll.server.sock.SockConnection
import vetalll.server.sock.SockPacketFactory

class GameClientConnection(
    packetParser: SockPacketFactory,
    private val crypt: GameConnectionCrypt,
) : SockConnection(packetParser, crypt) {
    fun sendInitPacket() {
        sendPacket(CryptInit(crypt.key))
    }
}