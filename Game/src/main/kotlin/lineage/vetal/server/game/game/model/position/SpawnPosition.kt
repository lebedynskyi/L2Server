package lineage.vetal.server.game.game.model.position


class SpawnPosition(
    x: Int,
    y: Int,
    z: Int,
    val heading: Int
) : Position(x, y, z) {
    constructor(location: Position) : this(location.x, location.y, location.z, 0)

    override fun toString(): String {
        return "SpawnPosition {x=$x, y=$y, z=$z, heading=$heading}"
    }

    companion object {
        val zero = SpawnPosition(0, 0, 0, 0)
    }
}