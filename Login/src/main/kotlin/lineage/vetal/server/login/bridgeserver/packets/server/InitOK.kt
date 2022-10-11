package lineage.vetal.server.login.bridgeserver.packets.server

import vetal.server.network.SendablePacket


class InitOK : SendablePacket() {
    override fun write() {
        writeC(0x01)

        writeD(1)
    }
}