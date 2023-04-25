package lineage.vetal.server.login.game.model.npc

import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.player.CreatureObject
import lineage.vetal.server.login.game.model.template.npc.NpcTemplate

class NpcObject(
    objectId: Int,
    val template: NpcTemplate,
    spawnData: NpcSpawnData?
) : CreatureObject(objectId, template.name, spawnData?.spawnPosition ?: SpawnPosition.zero) {

    override val status: NpcStatus = NpcStatus(template)
}