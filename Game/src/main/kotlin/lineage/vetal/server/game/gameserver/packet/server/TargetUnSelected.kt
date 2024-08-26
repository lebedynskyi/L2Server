package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class TargetUnSelected(
    private var objectId: Int,
    private val x: Int,
    private val y: Int,
    private val z: Int,
) : GameServerPacket() {
    override val opCode: Byte = 0x2A

    override fun write() {
        writeD(objectId)
        writeD(x)
        writeD(y)
        writeD(z)
    }
}