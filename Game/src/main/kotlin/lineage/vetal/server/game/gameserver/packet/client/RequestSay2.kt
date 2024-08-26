package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.model.player.SayType
import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestSay2 : GamePacket() {
    lateinit var text: String
    var typeId: Int = -1
    var targetName: String? = null

    override fun read() {
        text = readS()
        typeId = readD()
        if (typeId == SayType.TELL.ordinal) {
            targetName = readS()
        }
    }
}