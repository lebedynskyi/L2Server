package lineage.vetal.server.login.game.model.npc

import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.template.NpcTemplate
import java.util.*

class Npc(
    id: UUID,
    name: String,
    position: SpawnLocation,
    val template: NpcTemplate,
) : Creature(id, name, position) {
    override val status: NpcStatus = NpcStatus(template)
}