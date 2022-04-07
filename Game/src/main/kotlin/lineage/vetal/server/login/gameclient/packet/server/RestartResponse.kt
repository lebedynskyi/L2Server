package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.login.gameclient.packet.GameServerPacket

class RestartResponse private constructor(
    private val result: Int
) : GameServerPacket() {
    override fun write() {
        writeC(0x5f)
        writeD(result)
    }

    companion object {
        val STATIC_PACKET_OK = RestartResponse(1)
        val STATIC_PACKET_FAIL = RestartResponse(0)
    }
}