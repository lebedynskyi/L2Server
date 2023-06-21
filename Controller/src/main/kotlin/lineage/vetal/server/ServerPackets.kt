package lineage.vetal.server

import vetal.server.sock.WriteablePacket

class Action(
    private val speed: Int,
    private val bottomPressed: Boolean,
    private val topPressed: Boolean,
    private val leftPressed: Boolean,
    private val rightPressed: Boolean
) : WriteablePacket() {
    override val opCode: Byte = 0x3

    override fun write() {
        writeD(speed)
        writeC(if (topPressed) 1 else 0)
        writeC(if (bottomPressed) 1 else 0)
        writeC(if (leftPressed) 1 else 0)
        writeC(if (rightPressed) 1 else 0)
    }
}

class Info(
    private val planeConnected: Boolean
) : WriteablePacket() {
    override val opCode: Byte = 0x4

    override fun write() {
        writeC(if (planeConnected) 1 else 0)
    }
}