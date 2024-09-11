package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class Logout(
    private val account: String
) : GameServerPacket() {
    override val opCode: Byte = 0x03

    override fun write() {
        writeS(account)
    }
}