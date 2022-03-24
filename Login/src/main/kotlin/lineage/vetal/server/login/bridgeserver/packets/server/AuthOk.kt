package lineage.vetal.server.login.bridgeserver.packets.server

import lineage.vetal.server.core.server.SendablePacket

class AuthOk: SendablePacket() {
    override fun write() {
        writeC(0x02)

        writeD(1)
    }
}