package lineage.vetal.server.game.game.model.player

import lineage.vetal.server.game.game.model.position.SpawnPosition

// Base class for all objects that can be controled by player. Summon /  servitor.
abstract class Playable(
    objectId: Int,
    name: String,
    position: SpawnPosition
) : CreatureObject(objectId, name, position)