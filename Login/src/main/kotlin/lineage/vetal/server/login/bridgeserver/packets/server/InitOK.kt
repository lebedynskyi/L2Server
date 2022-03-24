package lineage.vetal.server.login.bridgeserver.packets.server

import lineage.vetal.server.core.server.SendablePacket

class InitOK : SendablePacket() {
    override fun write() {
        writeC(0x01)

        writeD(1)
    }
}