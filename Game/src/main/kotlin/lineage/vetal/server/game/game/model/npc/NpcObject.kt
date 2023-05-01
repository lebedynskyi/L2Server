package lineage.vetal.server.game.game.model.npc

import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.template.npc.NpcTemplate

class NpcObject(
    objectId: Int,
    val template: NpcTemplate,
    spawnPosition: SpawnPosition
) : CreatureObject(objectId, template.name, spawnPosition) {

    override val stats: NpcStatus = NpcStatus(template)
}