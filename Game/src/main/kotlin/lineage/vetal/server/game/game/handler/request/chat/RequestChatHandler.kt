package lineage.vetal.server.game.game.handler.request.chat

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.player.SayType
import lineage.vetal.server.game.gameserver.packet.server.CreatureSay

private const val TAG = "RequestChatHandler"

class RequestChatHandler(
    private val context: GameContext,
) {
    fun onRequestSay(player: PlayerObject, text: String, sayTypeId: Int, targetName: String?) {
        if (sayTypeId < 0 || sayTypeId > SayType.entries.size) return
        if (text.isBlank() || text.length > 100) return

        // Check player role. Chat ban ? Player is gm ? and etc
        // Check chat type? Send different messages?
        // Check command?
        val sayType = SayType.entries[sayTypeId]
        val message = text.replace("\\\\n", "")
        if (sayType == SayType.HERO_VOICE) {
            context.broadcaster.broadCast(CreatureSay(sayType, text))
        } else {
            context.broadcaster.broadCast(player.region, CreatureSay(player, sayType, message))
        }
    }

    fun announce(text: String) {
        context.broadcaster.broadCast(CreatureSay(SayType.ANNOUNCEMENT, text))
        writeDebug(TAG, "Announce -> $text")
    }
}