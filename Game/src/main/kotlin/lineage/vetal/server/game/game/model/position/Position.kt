package lineage.vetal.server.game.game.model.position

import lineage.vetal.server.game.xml.StatSet

open class Position(
    val x: Int,
    val y: Int,
    val z: Int
) {
    constructor(loc: Position) : this(loc.x, loc.y, loc.z)

    constructor(loc: StatSet) : this(
        loc.getInteger("x"),
        loc.getInteger("y"),
        loc.getInteger("z")
    )

    override fun toString(): String {
        return "Pos: $x, $y, $z"
    }

    override fun hashCode(): Int {
        return x xor y xor z
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    companion object {
        val zero = Position(0, 0, 0)
    }
}