package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class LeaveWorld : GameServerPacket() {
    override val opCode: Byte = 0x7e

    override fun write() {
    }
}