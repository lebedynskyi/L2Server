package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.position.SpawnPosition

// Base class for all objects that can be controled by player. Summon /  servitor.
abstract class Playable(
    objectId: Int,
    name: String,
    position: SpawnPosition
) : Creature(objectId, name, position)