package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.gameserver.packet.GameServerPacket

class LeaveWorld : GameServerPacket() {
    override fun write() {
        writeC(0x7e)
    }
}