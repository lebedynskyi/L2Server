package lineage.vetal.server.login.game.model.npc

import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.template.npc.NpcTemplate

class Npc(
    objectId: Int,
    val template: NpcTemplate,
    spawnData: NpcSpawnData?
) : Creature(objectId, template.name, spawnData?.spawnPosition ?: SpawnPosition.zero) {

    override val status: NpcStatus = NpcStatus(template)
}