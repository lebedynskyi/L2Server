package lineage.vetal.server.login.game.model.player

import java.util.UUID

abstract class Playable(
    id: UUID,
    name: String,
) : Creature(id, name) {

}