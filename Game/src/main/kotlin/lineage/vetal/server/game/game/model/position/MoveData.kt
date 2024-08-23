package lineage.vetal.server.game.game.model.position

data class MoveData(
    val destination: Position,
    var lastTime: Long,
)