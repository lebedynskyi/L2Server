package lineage.vetal.server.game.game.model.npc

import lineage.vetal.server.game.game.model.player.status.CreatureStats
import lineage.vetal.server.game.game.model.template.npc.NpcTemplate

class NpcStatus(
    private val npcTemplate: NpcTemplate
) : CreatureStats(npcTemplate) {
    override fun getBaseSwimSpeed() = 0
}