package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.server.SendablePacket

class RestartResponse private constructor(
    private val result: Int
) : SendablePacket() {
    override fun write() {
        writeC(0x5f)
        writeD(result)
    }

    companion object {
        val STATIC_PACKET_OK = RestartResponse(1)
        val STATIC_PACKET_FAIL = RestartResponse(0)
    }
}