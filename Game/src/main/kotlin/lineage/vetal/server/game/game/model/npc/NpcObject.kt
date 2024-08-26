package lineage.vetal.server.game.game.model.npc

import lineage.vetal.server.game.game.model.behaviour.NPCBehaviour
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.template.npc.NpcTemplate

open class NpcObject(
    objectId: Int,
    private val npcTemplate: NpcTemplate,
    override var position: SpawnPosition,
    override val name: String = npcTemplate.name,
    override val behaviour: NPCBehaviour = NPCBehaviour(),
) : CreatureObject(objectId, behaviour, npcTemplate, position) {

    override val template: NpcTemplate = npcTemplate

    override val stats: NpcStatus = NpcStatus(npcTemplate)
}