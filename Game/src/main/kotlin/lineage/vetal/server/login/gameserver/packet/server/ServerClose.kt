package lineage.vetal.server.login.gameserver.packet.server

import vetal.server.network.SendablePacket


class ServerClose private constructor() : SendablePacket() {
    override fun write() {
        writeC(0x26);
    }

    companion object {
        val STATIC_PACKET = ServerClose()
    }
}