package lineage.vetal.server.login.game.model.position

import lineage.vetal.server.login.xml.StatSet


open class Position {
    @Volatile
    var x: Int

    @Volatile
    var y: Int

    @Volatile
    var z: Int

    constructor(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(loc: Position) {
        x = loc.x
        y = loc.y
        z = loc.z
    }

    constructor(loc: StatSet) {
        x = loc.getInteger("x")
        y = loc.getInteger("y")
        z = loc.getInteger("z")
    }

    override fun toString(): String {
        return "Pos: $x, $y, $z"
    }

    override fun hashCode(): Int {
        return x xor y xor z
    }

    override fun equals(o: Any?): Boolean {
        if (o is Position) {
            val loc = o
            return loc.x == x && loc.y == y && loc.z == z
        }
        return false
    }

    operator fun set(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun set(loc: Position) {
        x = loc.x
        y = loc.y
        z = loc.z
    }

    fun clean() {
        x = 0
        y = 0
        z = 0
    }

    companion object {
        val ZERO_LOC = Position(0, 0, 0)
    }
}