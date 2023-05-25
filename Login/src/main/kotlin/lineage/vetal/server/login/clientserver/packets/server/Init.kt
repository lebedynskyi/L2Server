package lineage.vetal.server.login.clientserver.packets.server

import vetal.server.sock.WriteablePacket

class Init(
    private val sessionId: Int,
    private val publicKey: ByteArray,
    private val blowFishKey: ByteArray
) : WriteablePacket() {
    override val opCode: Byte = 0x00

    override fun write() {
        // session id
        writeD(sessionId)

        // protocol revision
        writeD(0x0000c621)

        // RSA Public Key
        writeB(publicKey)
        System.err.println(publicKey.toString())

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