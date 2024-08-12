package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class ActionFailed : GameServerPacket() {
    override val opCode: Byte = 0x25

    override fun write() {

    }

    companion object {
        val STATIC_PACKET = ActionFailed()
    }
}