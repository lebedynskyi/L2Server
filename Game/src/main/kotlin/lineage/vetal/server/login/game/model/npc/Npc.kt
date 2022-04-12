package lineage.vetal.server.login.game.model.npc

import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.template.NpcTemplate
import java.util.*

class Npc(
    id: UUID,
    val template: NpcTemplate,
    val spawnData: SpawnData?
) : Creature(id, template.name, spawnData?.spawnLocation ?: SpawnLocation(0, 0, 0, 0)) {
    override val status: NpcStatus = NpcStatus(template)

    init {
        
    }
}