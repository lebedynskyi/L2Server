package lineage.vetal.server.game.game.manager.chat

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.player.SayType
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.server.CreatureSay

private const val TAG = "ChatManager"

class ChatManager(
    private val context: GameContext,
) {
    fun playerSay(client: GameClient, text: String, sayTypeId: Int, targetName: String?) {
        if (sayTypeId < 0 || sayTypeId > SayType.entries.size) return
        val player = client.player ?: return
        if (text.isBlank() || text.length > 100) return

        // Check player role. Chat ban ? Player is gm ? and etc
        // Check chat type? Send different messages?
        // Check command?
        val sayType = SayType.entries[sayTypeId]
        val message = text.replace("\\\\n", "")
        if (sayType == SayType.HERO_VOICE) {
            context.worldManager.broadCast(CreatureSay(sayType, text))
        } else {
            context.worldManager.broadCast(player.region, CreatureSay(player, sayType, message))
        }
    }

    fun announce(text: String) {
        context.worldManager.broadCast(CreatureSay(SayType.ANNOUNCEMENT, text))
        writeDebug(TAG, "Announce -> $text")
    }
}