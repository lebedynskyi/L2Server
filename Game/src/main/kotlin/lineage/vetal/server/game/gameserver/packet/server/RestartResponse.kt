package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class RestartResponse private constructor(
    private val result: Int
) : GameServerPacket() {
    override val opCode: Byte = 0x5f

    override fun write() {
        writeD(result)
    }

    companion object {
        val STATIC_PACKET_OK = RestartResponse(1)
        val STATIC_PACKET_FAIL = RestartResponse(0)
    }
}