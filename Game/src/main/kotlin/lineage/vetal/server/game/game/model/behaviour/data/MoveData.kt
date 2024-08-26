package lineage.vetal.server.game.game.model.behaviour.data

import lineage.vetal.server.game.game.model.position.Position

data class MoveData(
    val destination: Position,
    var lastTime: Long,
)