package lineage.vetal.server.game.game.model.npc

import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.template.npc.NpcTemplate

class NpcMonsterObject(
    objectId: Int,
    override val template: NpcTemplate,
    override var position: SpawnPosition,
    override val name: String = template.name,
) : NpcObject(objectId, template, position, name) {

    override val isAutoAttackable: Boolean = true
}