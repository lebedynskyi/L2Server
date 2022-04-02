package lineage.vetal.server.login.game.model.location

import lineage.vetal.server.login.xml.StatSet

open class Location {
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

    constructor(loc: Location) {
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
        return "LOC: $x, $y, $z"
    }

    override fun hashCode(): Int {
        return x xor y xor z
    }

    override fun equals(o: Any?): Boolean {
        if (o is Location) {
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

    fun set(loc: Location) {
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
        val ZERO_LOC = Location(0, 0, 0)
    }
}