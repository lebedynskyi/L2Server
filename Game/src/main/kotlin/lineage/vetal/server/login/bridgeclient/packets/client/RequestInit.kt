package lineage.vetal.server.login.bridgeclient.packets.client

import lineage.vetal.server.core.server.SendablePacket

class RequestInit(
    private var serverId: Int
) : SendablePacket() {

    override fun write() {
        writeC(0x01)
        writeD(serverId)
    }
}