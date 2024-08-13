package lineage.vetal.server.game.game.model.npc

import lineage.vetal.server.game.game.model.behaviour.NPCBehaviour
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.template.npc.NpcTemplate

class NpcObject(
    objectId: Int,
    override val template: NpcTemplate,
    override var position: SpawnPosition,
    override val behaviour: NPCBehaviour = NPCBehaviour(),
) : CreatureObject(objectId, behaviour, template, position) {

    override var name: String
        get() = template.name
        set(value) {}

    override val stats: NpcStatus = NpcStatus(template)
}