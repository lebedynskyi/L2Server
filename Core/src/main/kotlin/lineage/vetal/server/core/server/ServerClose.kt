package lineage.vetal.server.core.server


class ServerClose private constructor() : SendablePacket() {
    override fun write() {
        writeC(0x26);
    }

    companion object {
        val STATIC_PACKET = ServerClose()
    }
}