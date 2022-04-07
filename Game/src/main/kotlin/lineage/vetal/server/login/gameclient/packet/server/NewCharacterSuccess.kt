package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.login.game.model.template.CharTemplate
import lineage.vetal.server.login.gameclient.packet.GameServerPacket

class NewCharacterSuccess(
    private val templated: List<CharTemplate>
) : GameServerPacket() {
    override fun write() {
        writeC(0x17)
        writeD(templated.size)

        templated.forEach {
            writeD(it.charClass.race.id)
            writeD(it.charClass.id)
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