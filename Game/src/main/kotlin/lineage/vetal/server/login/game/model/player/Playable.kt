package lineage.vetal.server.login.game.model.player

import java.util.UUID

open class Playable(
    id: UUID,
    name: String,
) : Creature(id, name) {

}