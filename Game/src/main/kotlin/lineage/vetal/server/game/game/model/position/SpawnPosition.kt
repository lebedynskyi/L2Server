package lineage.vetal.server.game.game.model.position


class SpawnPosition(
    x: Int, y: Int, z: Int,
    val heading: Int = 0
) : Position(x, y, z) {
    constructor(pos: Position, heading: Int = 0) : this(pos.x, pos.y, pos.z, heading)

    override fun toString(): String {
        return "SpawnPosition {x=$x, y=$y, z=$z, h=$heading}"
    }

    companion object {
        val zero = SpawnPosition(0, 0, 0, 0)
    }
}