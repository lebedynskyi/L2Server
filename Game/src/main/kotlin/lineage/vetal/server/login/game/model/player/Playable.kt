package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.position.SpawnPosition
import java.util.UUID

// Base class for all objects that can be controled by player. Summon /  servitor.
abstract class Playable(
    id: UUID,
    name: String,
    position: SpawnPosition
) : Creature(id, name, position)