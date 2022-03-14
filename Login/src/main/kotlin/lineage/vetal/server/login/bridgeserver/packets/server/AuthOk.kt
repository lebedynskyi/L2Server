package lineage.vetal.server.login.bridgeserver.packets.server

import lineage.vetal.server.core.server.SendablePacket

class AuthOk: SendablePacket() {
    override fun write() {
        writeD(0x01)
    }
}