package lineage.vetal.server.game.bridgeclient.packets.client

import vetal.server.network.SendablePacket


class RequestInit(
    private var serverId: Int
) : SendablePacket() {

    override fun write() {
        writeC(0x01)

        writeD(serverId)
    }
}