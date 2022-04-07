package lineage.vetal.server.login.game.model.npc

import lineage.vetal.server.login.game.model.player.status.CreatureStatus
import lineage.vetal.server.login.game.model.template.NpcTemplate

class NpcStatus(
    private val npcTemplate: NpcTemplate
) : CreatureStatus(npcTemplate) {
    override fun getBaseSwimSpeed() = 0
}