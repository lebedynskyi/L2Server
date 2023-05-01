package lineage.vetal.server.login.game.model.npc

import lineage.vetal.server.login.game.model.player.status.CreatureStats
import lineage.vetal.server.login.game.model.template.npc.NpcTemplate

class NpcStatus(
    private val npcTemplate: NpcTemplate
) : CreatureStats(npcTemplate) {
    override fun getBaseSwimSpeed() = 0
}