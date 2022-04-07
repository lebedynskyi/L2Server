package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.location.SpawnLocation
import java.util.UUID

abstract class Playable(
    id: UUID,
    name: String,
    position: SpawnLocation
) : Creature(id, name, position) {

}