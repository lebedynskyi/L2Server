package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.server.SendablePacket


class ServerClose private constructor() : SendablePacket() {
    override fun write() {
        writeC(0x26);
    }

    companion object {
        val STATIC_PACKET = ServerClose()
    }
}