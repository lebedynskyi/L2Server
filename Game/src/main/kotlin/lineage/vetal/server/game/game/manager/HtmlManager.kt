package lineage.vetal.server.game.game.manager

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.server.HtmlMessage

private const val DUMMY_HTML = "<html><body><br>This is dummy html. It works!</body></html>"

class HtmlManager(
    private val gameContext: GameContext
) {
    fun openHtml(player: PlayerObject, creature: CreatureObject) {
        player.sendPacket(HtmlMessage(creature.objectId, DUMMY_HTML))
    }
}
