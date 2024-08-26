package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class TargetSelected(
    private var objectId: Int,
    private val targetObjId: Int,
    private val x: Int,
    private val y: Int,
    private val z: Int,
) : GameServerPacket() {
    override val opCode: Byte = 0x29

    override fun write() {
        writeD(objectId)
        writeD(targetObjId)
        writeD(x)
        writeD(y)
        writeD(z)
    }
}