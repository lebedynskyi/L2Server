package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.login.model.template.CharacterTemplate

class NewCharacterSuccess(
    private val templated: List<CharacterTemplate>
) : SendablePacket() {
    override fun write() {
        writeC(0x17)
        writeD(templated.size)

        templated.forEach {
            writeD(it.classId.race.id)
            writeD(it.classId.id)
            writeD(0x46)
            writeD(it.baseSTR)
            writeD(0x0a)
            writeD(0x46)
            writeD(it.baseDEX)
            writeD(0x0a)
            writeD(0x46)
            writeD(it.baseCON)
            writeD(0x0a)
            writeD(0x46)
            writeD(it.baseINT)
            writeD(0x0a)
            writeD(0x46)
            writeD(it.baseWIT)
            writeD(0x0a)
            writeD(0x46)
            writeD(it.baseMEN)
            writeD(0x0a)
        }
    }
}