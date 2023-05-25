package lineage.vetal.server.game.gameserver.packet.server

import vetal.server.sock.WriteablePacket


class ServerClose private constructor() : WriteablePacket() {
    override val opCode: Byte = 0x26

    override fun write() {
    }

    companion object {
        val STATIC_PACKET = ServerClose()
    }
}