package lineage.vetal.server.login.packets.server

import lineage.vetal.server.core.server.SendablePacket

class Init(
    val sessionId: Int,
    val publicKey: ByteArray,
    val blowFishKey: ByteArray
) : SendablePacket() {

    override fun write() {
        // init packet id
        writeC(0x00)

        // session id
        writeD(sessionId)

        // protocol revision
        writeD(0x0000c621)

        // RSA Public Key
        writeB(publicKey)

        // unk GG related?
        writeD(0x29DD954E)
        writeD(0x77C39CFC)
        writeD(0x97ADB620.toInt())
        writeD(0x07BDE0F7)

        // BlowFish key
        writeB(blowFishKey)

        // null termination ;)
        writeC(0x00)
    }
}