package lineage.vetal.server.login.model.template

import lineage.vetal.server.login.model.*

class PlayerTemplate(set: StatsSet) : CreatureTemplate(set) {
    val classId: ClassId = ClassId.VALUES[id]
    val fallHeight: Int = set.getInteger("falling_height", 333)
    val baseSwimSpeed: Int = set.getInteger("swimSpd", 1)
    val classBaseLevel: Int = set.getInteger("baseLvl")
    val fists: Int = set.getInteger("fists")

    val items: List<ItemTemplate> = set.getList("items")
    val skills: List<SkillTemplate> = set.getList("skills")

    private val _collisionRadiusFemale: Double = set.getDouble("radiusFemale")
    private val _collisionHeightFemale: Double = set.getDouble("heightFemale")
    private val _spawnLocations: List<Location> = set.getList("spawnLocations")
    private val _hpTable: DoubleArray = set.getDoubleArray("hpTable")
    private val _mpTable: DoubleArray = set.getDoubleArray("mpTable")
    private val _cpTable: DoubleArray = set.getDoubleArray("cpTable")

    val randomSpawn: Location get() = _spawnLocations.random()

    fun getCollisionRadiusBySex(sex: Sex): Double {
        return if (sex === Sex.MALE) collisionRadius else _collisionRadiusFemale
    }

    fun getCollisionHeightBySex(sex: Sex): Double {
        return if (sex === Sex.MALE) collisionHeight else _collisionHeightFemale
    }

    override fun getBaseHpMax(level: Int): Double {
        return _hpTable[level - 1]
    }

    override fun getBaseMpMax(level: Int): Double {
        return _mpTable[level - 1]
    }

    fun getBaseCpMax(level: Int): Double {
        return _cpTable[level - 1]
    }

    fun findSkill(id: Int, level: Int): SkillTemplate? {
        return skills.asSequence()
            .filter { s: SkillTemplate -> s.id == id && s.lvl == level }
            .firstOrNull()
    }
}