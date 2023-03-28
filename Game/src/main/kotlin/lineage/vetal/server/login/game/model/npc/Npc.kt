package lineage.vetal.server.login.game.model.npc

import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.template.npc.NpcTemplate
import java.util.*

class Npc(
    id: UUID,
    val template: NpcTemplate,
    val spawnData: NpcSpawnData?
) : Creature(id, template.name, spawnData?.spawnPosition ?: SpawnPosition(0, 0, 0, 0)) {
    override val status: NpcStatus = NpcStatus(template)
}