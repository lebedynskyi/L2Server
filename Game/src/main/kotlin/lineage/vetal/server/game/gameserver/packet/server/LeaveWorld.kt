package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class LeaveWorld : GameServerPacket() {
    override fun write() {
        writeC(0x7e)
    }
}