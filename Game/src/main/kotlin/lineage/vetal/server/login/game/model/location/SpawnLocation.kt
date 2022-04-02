package lineage.vetal.server.login.game.model.location


class SpawnLocation(
    x: Int,
    y: Int,
    z: Int,
    val heading: Int
) : Location(x, y, z) {
    constructor(location: Location) : this(location.x, location.y, location.z, 0)
}