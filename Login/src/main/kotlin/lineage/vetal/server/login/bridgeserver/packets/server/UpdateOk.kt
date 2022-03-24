package lineage.vetal.server.login.bridgeserver.packets.server

import lineage.vetal.server.core.server.SendablePacket

class UpdateOk: SendablePacket() {
    override fun write() {
        writeC(0x03)

        writeD(1)
    }
}