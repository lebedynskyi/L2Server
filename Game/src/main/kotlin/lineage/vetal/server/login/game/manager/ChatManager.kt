package lineage.vetal.server.login.game.manager

import lineage.vetal.server.login.game.model.player.SayType
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.server.CreatureSay

class ChatManager(
    private val worldManager: WorldManager
) {
    fun playerSay(client: GameClient, text: String, sayTypeId: Int, targetName: String?) {
        if (sayTypeId < 0 || sayTypeId > SayType.values().size) return
        val player = client.player ?: return
        if (text.isBlank() || text.length > 100) return

        // Check player role. Chat ban ? Player is gm ? and etc
        // Check chat type? Send different messages?
        val sayType = SayType.values()[sayTypeId]
        val message = text.replace("\\\\n", "")
        if(sayType == SayType.HERO_VOICE) {
            worldManager.broadCast(CreatureSay(sayType, text))
        } else {
            player.region.broadCast(CreatureSay(player, sayType, message))
        }
    }

    fun announce(text: String, type: SayType) {
        worldManager.broadCast(CreatureSay(type, text))
    }
}