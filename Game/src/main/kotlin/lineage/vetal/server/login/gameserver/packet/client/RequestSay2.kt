package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.game.model.player.SayType
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket

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

    override fun execute(client: GameClient, context: GameContext) {
        context.chatManager.playerSay(client, text, typeId, targetName)
    }
}