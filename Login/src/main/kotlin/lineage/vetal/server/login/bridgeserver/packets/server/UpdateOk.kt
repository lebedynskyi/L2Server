package lineage.vetal.server.login.bridgeserver.packets.server

import vetal.server.network.SendablePacket

class UpdateOk : SendablePacket() {
    override fun write() {
        writeC(0x03)
        writeD(1)
    }
}