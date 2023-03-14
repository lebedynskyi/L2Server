package lineage.vetal.server.login.game.manager

import lineage.vetal.server.login.game.model.player.SayType
import lineage.vetal.server.login.gameserver.GameClient

class ChatManager {
    fun playerSay(client: GameClient, text: String, sayTypeId: Int, targetName: String?) {
        if (sayTypeId <0 || sayTypeId > SayType.values().size) return
        val player = client.player ?: return
        if (text.isBlank() || text.length > 100) return

        //Check player role. Chat ban ? Player is gm ? and etc
        val sayType = SayType.values()[sayTypeId]
        val message = text.replace("\\\\n", "")
        player.say(sayType, message)
    }
}