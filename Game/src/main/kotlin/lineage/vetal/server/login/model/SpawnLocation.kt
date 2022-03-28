package lineage.vetal.server.login.model


class SpawnLocation(
    x: Int,
    y: Int,
    z: Int,
    val heading: Int
) : Location(x, y, z)