package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

open class CreateCharFail(private val _error: Int) : GameServerPacket() {
    override val opCode: Byte = 0x1a

    override fun write() {
        writeD(_error)
    }

    companion object {
        val REASON_CREATION_FAILED = CreateCharFail(0x00)
        val REASON_TOO_MANY_CHARACTERS = CreateCharFail(0x01)
        val REASON_NAME_ALREADY_EXISTS = CreateCharFail(0x02)
        val REASON_16_ENG_CHARS = CreateCharFail(0x03)
        val REASON_INCORRECT_NAME = CreateCharFail(0x04)
        val REASON_CREATE_NOT_ALLOWED = CreateCharFail(0x05)
        val REASON_CHOOSE_ANOTHER_SVR = CreateCharFail(0x06)
    }
}