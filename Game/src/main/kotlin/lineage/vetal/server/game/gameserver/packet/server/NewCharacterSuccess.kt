package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.template.pc.CharTemplate
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class NewCharacterSuccess(
    private val templates: List<CharTemplate>
) : GameServerPacket() {
    override val opCode: Byte = 0x17

    override fun write() {
        writeD(templates.size)

        templates.forEach {
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